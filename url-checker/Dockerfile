FROM python:3.10.13-slim
WORKDIR /usr/src/url-checker/

RUN pip install --upgrade pip
COPY . /usr/src/url-checker/

COPY ./requirements.txt /usr/src/url-checker
RUN pip wheel --no-cache-dir --no-deps --wheel-dir /usr/src/url-checker/wheels -r requirements.txt

RUN pip install --upgrade pip
RUN pip install -r requirements.txt

ENTRYPOINT ["python3", "server.py", "-prod"]