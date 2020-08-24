import os
from flask import Flask
from flask import url_for, request
from worker import celery
from celery.result import AsyncResult
import celery.states as states

env=os.environ
app = Flask(__name__)

@app.route('/service-status')
def status():
    json = {"status": "up"}
    return json

@app.route('/add/<int:param1>/<int:param2>')
def add(param1,param2):
    json = {"hi": "yes"}
    task = celery.send_task('mytasks.add', args=[param1, param2, json], kwargs={})
    return "<a href='{url}'>check status of {id} </a>".format(id=task.id,
                url=url_for('check_task',id=task.id,_external=True))

@app.route('/longTask', methods=['POST'])
def longTask():
    json = request.json
    task = celery.send_task('mytasks.longtask', args=[json], kwargs={})
    return "<a href='{url}'>check status of {id} </a>".format(id=task.id,
                url=url_for('check_task',id=task.id,_external=True))

@app.route('/check/<string:id>')
def check_task(id):
    res = celery.AsyncResult(id)
    if res.state==states.RECEIVED:
        return res.state
    elif res.state==states.STARTED:
        return res.state
    else:
        return str(res.result)
if __name__ == '__main__':
    app.run(debug=env.get('DEBUG',True),
            port=int(env.get('PORT',5000)),
            host=env.get('HOST','0.0.0.0')
    )
