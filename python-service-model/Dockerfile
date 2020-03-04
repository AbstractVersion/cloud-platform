FROM python:3
RUN pip install --upgrade pip
RUN pip install requests
RUN pip install py_eureka_client
RUN pip install flask
COPY ./app /app
CMD [ "python3", "/app/main.py" ]
