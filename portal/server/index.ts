import express, { Request, Response } from 'express';
import * as path from 'path';
import { Log } from './logger';
import { JwtValidator } from './jwt-validator';
import { getIndexHtml } from './content';


const log = new Log();
const indexHtml = getIndexHtml();
const jwtValidator = new JwtValidator(log.logger, process.env.JWKS_URI || 'http://localhost:9000/auth-certs');

const serve = (req: Request, res: Response) => {
  res.send(indexHtml);
}

const app = express();
app.use(log.middleware);
app.use(express.static(path.join(__dirname, '..', 'build'), {index: false}));

app.get(/^\/.+?\/app($|\/.*)/, jwtValidator.validator, serve);
app.get('/*', serve);

const port = 3000;
app.listen(port, () => {
  console.log(`Server now listening on port: ${port}`);
});
