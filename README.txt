# Learning course telegram bot

## Setup

### Local

1. setup redis
1.1 config https://dashboard.heroku.com/apps/course-cms/datastores/52ade2bc-d0f4-4fd0-82b5-af0e484ea878/settings
1.2 update application.yaml (spring.data.redis)
2. start EnergyCourseBotApplication
3. ngrok http 5000 -> host name
4. update host name in
4.0 application.yaml (telegram.webhook-url)
4.1 http-client.private.env.json
4.2 https://m.wayforpay.com/uk/mportal/payments/button-update?id=22620396
5.call webhook.http


### Remote
