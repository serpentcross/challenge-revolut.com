Hello! My Name is Vladimir and this is my admission test for company Revolut.

Getting started.

1. Clone repository:

_git clone https://github.com/serpentcross/challenge-revolut.com.git_

2. Build application:

_mvn clean install_

3. How to use:

After installation will complete and tests are passed, you may find a single .jar-file in target-folder

Run it:

java -jar revolut-1.0.jar

Then open your web-browser and follow to URL:

http://localhost:2222/api

listing information about customer:

http://localhost:2222/api/customer?id=1 (for example)

make a transfer transaction:

http://localhost:2222/api/transfer?senderId=1&accountId=1&amount=100&phone=9769393020

result:

########################################################################
#                                                                      #
#                         TRANSACTION HISTORY                          #
#                                                                      #
########################################################################

Transaction # 1 | Thu Oct 26 10:48:59 MSK 2017
-----------------------------------------------------------------------
account: 1
balance: 2900.0
-----------------------------------------------------------------------
transfer amount: 100.0
currency: GBP
receiver: 9769393020
-----------------------------------------------------------------------

Thank you!