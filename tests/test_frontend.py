from selenium import webdriver
from selenium.webdriver.chrome.service import Service as ChromeService

from chromedriver_py import binary_path
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By

from time import sleep
from tests.config import config
from assertpy import assert_that

FRONTEND_URL = config['fe.endpoint']
URL = 'https://www.google.com/search?q=novak+djokovic&oq=novak+dj&aqs=chrome.0' \
      '.0i355i433i512j46i433i512j69i57j0i512l7&sourceid=chrome&ie=UTF-8 '
      
# Set options for not prompting DevTools information
options = Options()
options.add_experimental_option("excludeSwitches", ["enable-logging"])

svc = ChromeService(executable_path=binary_path)
driver = webdriver.Chrome(service=svc, options=options)

driver.get(FRONTEND_URL)
sleep(3)

# Find element using element's id attribute
driver.find_element(By.ID, "url-bar").send_keys(URL)
driver.find_element(By.ID, "shorten-url-button").click()
sleep(5)

# Assert the results
text = driver.find_element(By.CLASS_NAME, "shortener__viewShot").text
assert_that(text).is_not_empty()

short_url_identifier = text.split('\n')[0].split('/')[-1]
assert_that(len(short_url_identifier)).is_equal_to(11)

# Close the driver
driver.quit()
