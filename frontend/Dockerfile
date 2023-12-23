FROM node:12-stretch

RUN addgroup --system url-shortener-frontend && adduser --system --group url-shortener-frontend

USER url-shortener-frontend

WORKDIR /home/url-shortener-frontend

# or COPY --chown=url-shortener:url-shortener package-lock.json package.json ./
COPY --chown=url-shortener-frontend:url-shortener-frontend . .

RUN npm ci

# start an app
CMD ["npm", "start"]