# Twitter metrics

#### (Just a simple task to get a little practice with twitter api and scrooge)

This app connects to Twitter Streaming API (metrics/Main) and collects naive statistics of top words by language.
It also sets up a Thrift server to expose these statistics (metrics/StatService).
There is also a test client, which requests these stats (test/TestClient).