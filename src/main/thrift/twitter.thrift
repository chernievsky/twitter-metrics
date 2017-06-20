#@namespace scala test

struct Tweet {
    1: i64 id
    2: string text
    3: string lang
}

service TwitterStatistics {
    Tweet getTweet(
        1: i64 id
    )
}