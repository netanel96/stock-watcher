import bs4 as bs
import requests
import yfinance as yf
import sys
import datetime

# get s&p 500 tickers
resp = requests.get('http://en.wikipedia.org/wiki/List_of_S%26P_500_companies')
soup = bs.BeautifulSoup(resp.text, 'lxml')
table = soup.find('table', {'class': 'wikitable sortable'})
tickers = []
for row in table.findAll('tr')[1:]:
    ticker = row.findAll('td')[0].text
    tickers.append(ticker)
tickers = [s.replace('\n', '') for s in tickers]
# print(tickers)
current_tickers = tickers[0:10]
# print(current_tickers)

# get tickers from input.
tickers_input = list()
if sys.argv.__len__() > 1:
    tickers_input = sys.argv[1:]
else:
    tickers_input = current_tickers
print("tickers input:" + str(tickers_input))
# get tickers stock data
# start = datetime.datetime(2021,11,18)
# end = datetime.datetime.now()
data = yf.download(tickers_input, period="1d",
                   interval='1d', threads=3, progress=False, group_by='ticker')
# print(data)
number_of_rows_in_data = data.shape[0]
# print("******************my_results:*****")
dict_result = dict()
if tickers_input.__len__() > 1:
    for ticker in tickers_input:
        dict_result[ticker] = data[ticker].iloc[number_of_rows_in_data - 1, 3]
else:
    dict_result[tickers_input[0]] = data.iloc[number_of_rows_in_data - 1, 3]
sign_of_result = "END_RESULTS"
print(sign_of_result + str(dict_result) + sign_of_result)

# left in iloc is the row  which in 0 is the price and right in data.iloc[0,0] is the
# column which is the stock i want.
# print(data.iloc[0,1])

# a# group by ticker (to access via data['SPY'])
# (optional, default is 'column')
# group_by = 'ticker',
# print(data[tickers[0]])
# notice that in this way the close is the iloc[0, 3].
