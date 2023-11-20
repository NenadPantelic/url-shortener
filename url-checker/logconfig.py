import logging as log

log.basicConfig(level=log.INFO, filename="app.log",
                    format='%(asctime)s: %(levelname)s - %(message)s',
                    datefmt='%m/%d/%Y %I:%M:%S %p')


