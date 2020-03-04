FROM tiangolo/uwsgi-nginx-flask:python3.7
RUN pip install --upgrade pip
RUN pip install requests
RUN pip install py_eureka_client
RUN pip install flask
COPY ./app/main.py /app

