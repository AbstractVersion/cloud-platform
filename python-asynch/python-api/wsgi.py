from app import app
import sleuth, b3

if __name__ == "__main__":
    app.before_request(b3.start_span)
    app.after_request(b3.end_span)
    app.run()
