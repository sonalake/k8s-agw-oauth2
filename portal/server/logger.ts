import winston, { Logger } from 'winston';
import morgan, { StreamOptions } from 'morgan';


export class Log  {
  public logger: Logger;
  public middleware: ReturnType<typeof morgan>;

  constructor() {
    this.logger = winston.createLogger({
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
      write: (message) => this.logger.http(message),
    };
    this.middleware = morgan(':remote-addr - :remote-user [:date[clf]] ":method :url HTTP/:http-version" :status :res[content-length] :user-agent', { stream })
  }
}
