// List all jobs along with their associated triggers

import org.quartz.impl.StdSchedulerFactory
import org.quartz.impl.matchers.GroupMatcher

config = args[0]
scheduler = new StdSchedulerFactory(config).getScheduler()
try {
    keys = scheduler.getJobKeys(GroupMatcher.anyGroup())
    println("= Job List =")
    keys.forEach { jobKey ->
        triggers = scheduler.getTriggersOfJob(jobKey)
        if (triggers.isEmpty())
            println("$jobKey.name - no triggers")
        else
            println("$jobKey.name - ${triggers.collect { it.key.name }}")
    }
    println("Total of ${keys.size()} jobs found.")
} finally {
    scheduler.shutdown()
}