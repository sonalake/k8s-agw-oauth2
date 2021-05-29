FROM node:12.18.3


COPY ["package.json", "package-lock.json", "./"]
RUN ls
RUN npm ci
COPY . .

RUN npm run build

EXPOSE 3000

CMD ["npm", "run", "server"]
