## url-checker

This component has a protective role - it should check the safety of the URL before it is shortened, since malicious URls can be passed to shortening (if the user goes to that page when it's shortened, he can pick up an unwanted thing). If you want to run it, you can build the Docker container on your local machine or run it natively as a python development server (the improvement that should be made is to switch to some production-ready solution like gunicorn).

Steps (run it under `url-checker` folder)

```bash
python3 -m venv venv # create a virtual env
source venv/bin/activate # activate that venv
pip3 install -r requirements.txt # install necessary packages
python3 server.py # run the server
```
