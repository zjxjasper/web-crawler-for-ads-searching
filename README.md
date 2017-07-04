# web-crawler-for-ads-searching
A resilient web crawler using jsoup

The application will crawl all query from querylist /resource/rawQuery.txt 
and randomly choose a proxy from proxylistList given in /resource/proxylist.csv for each query.
In /resource , there are examples of proxylist and querylist;

The crawed data is stored in json format in crawl_result.json. (one example result is provided)
The application will log the query to be successful or failed, depending on if non-zeros number of products information are obtained.
it will iteratively re-query the failed queries from list until all of them are successed.
The log file is stored in both CrawlHistory.log (for iteration logs), and crawl_result.log (detailed log)

paging is supported by changing the parameters in /resource/crawling_env.inp, maximum paging range is set as 10.
jsoup, jackson, lucence libaries are used, details given in .iml file.
