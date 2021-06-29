import jwksClient, {
  CertSigningKey,
  JwksClient,
  RsaSigningKey
} from 'jwks-rsa';
import { Logger } from 'winston';
import { NextFunction, Request, Response } from 'express';
import jwt from 'jsonwebtoken';

export class JwtValidator {
  private client: JwksClient;
  public validator: (req: Request, res: Response, next: NextFunction) => void;

  private _validator(req: Request, res: Response, next: NextFunction) {
    const token = req.header('Authorization')?.replace('Bearer ', '');
    jwt.verify(token || '', this.getKey.bind(this), undefined, (error) => {
      if (error) {
        res.set('WWW-Authenticate', `Bearer error=${error.message}`);
        res.status(401).send(null);
      } else{
        next()
      }
    });
  }

  private getKey(header: any, callback: any) {
    this.client.getSigningKey(header.kid, (err, key) => {
      let signingKey = null;
      if (key) {
        signingKey = (key as CertSigningKey).publicKey || (key as RsaSigningKey).rsaPublicKey;
      } else {
        this.logger.error('Couldnt validate jwt');
      }
      callback(null, signingKey);
    });
  }

  constructor(private logger: Logger, private jwksUri: string) {
    logger.info(JSON.stringify({jwksUri: this.jwksUri}));
    this.client = jwksClient({jwksUri: this.jwksUri});
    this.validator = this._validator.bind(this);
  }
}

