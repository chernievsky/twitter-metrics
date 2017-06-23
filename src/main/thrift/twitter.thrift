#@namespace scala metrics

struct wordStat{
    1: string word
    2: i64 num
}

typedef list<wordStat> topWords

service TwitterStatistics {
    topWords getTopWords (
        1: string lang
        2: i32 n
    )
    i64 getNumByLang (
        1: string lang
    )
}
