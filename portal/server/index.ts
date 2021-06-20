import express from 'express';
import * as path from 'path';
import morgan, { StreamOptions } from 'morgan';
import jwt from 'jsonwebtoken';
import jwksClient, { CertSigningKey, RsaSigningKey } from 'jwks-rsa';
import winston from 'winston';

const logger = winston.createLogger({
  level: 'debug',
  format: winston.format.combine(
    winston.format.timestamp({ format: 'YYYY-MM-DD HH:mm:ss:ms' }),
    winston.format.printf(
      (info) => `${info.timestamp} ${info.level}: ${info.message}`,
    ),
  ),
  transports: [
    new winston.transports.Console(),
  ],
});
const stream: StreamOptions = {
  write: (message) => logger.http(message),
};

const jwksUri = process.env.JWKS_URI || 'http://localhost:8080/auth/realms/msagw/protocol/openid-connect/certs';
logger.info(JSON.stringify({jwksUri}));
const client = jwksClient({jwksUri});
const getKey = (header: any, callback: any) => {
  client.getSigningKey(header.kid, (err, key) => {
    let signingKey = null;
    if (key) {
      signingKey = (key as CertSigningKey).publicKey || (key as RsaSigningKey).rsaPublicKey;
    } else {
      logger.error('Couldnt validate jwt');
    }
    callback(null, signingKey);
  });
}

const app = express();
app.use(morgan(':remote-addr - :remote-user [:date[clf]] ":method :url HTTP/:http-version" :status :res[content-length] :user-agent', { stream }));

app.use(express.static(path.join(__dirname, '..', 'build')));

app.get(['/app', '/app/', '/app/*'], (req, res) => {
  const token = req.header('Authorization')?.replace('Bearer ', '');
  jwt.verify(token || '', getKey, undefined, (error) => {
    if (error) {
      res.set('WWW-Authenticate', `Bearer error=${error.message}`);
      res.status(401).send(null);
    } else{
      res.sendFile(path.join(__dirname, '..', 'build', 'index.html'));
    }
  });
});

app.get('/*', (req: any, res: any) => {
  console.log(req.headers);
  res.sendFile(path.join(__dirname, '..', 'build', 'index.html'));
});


const port = 3000;
app.listen(port, () => {
  console.log(`Server now listening on port: ${port}`);
});
