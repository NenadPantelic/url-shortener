from dotenv import dotenv_values
from argparse import ArgumentParser

# constants
DEV_PROFILE = 'dev'
PRODUCTION_PROFILE = 'prod'

DEV_PROFILE_CONFIG_FILE = 'tests/.env.dev'
PROD_PROFILE_CONFIG_FILE = 'tests/.env.prod'

parser = ArgumentParser()
parser.add_argument('-p', '--profile', help='The profile used to configure the setup of the server.')

args = parser.parse_args()
profile = args.profile

if profile == DEV_PROFILE:
    profile_config_file = DEV_PROFILE_CONFIG_FILE
elif profile == PRODUCTION_PROFILE:
    profile_config_file = PROD_PROFILE_CONFIG_FILE
else:
    print('The profile has not been provided, defaulting to DEV profile...')
    profile_config_file = DEV_PROFILE_CONFIG_FILE

config = dotenv_values(profile_config_file)
