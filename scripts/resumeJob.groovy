// Resume all triggers for a job
// args: jobName
import org.quartz.JobKey
import org.quartz.TriggerKey
import org.quartz.impl.StdSchedulerFactory
import zemian.quartzextra.QuartzServer

config = System.getProperty(QuartzServer.CONFIG_KEY, QuartzServer.DEFAULT_CONFIG)
scheduler = new StdSchedulerFactory(config).getScheduler()
try {
    jobName = args[0]
    jobKey = JobKey.jobKey(jobName)
    scheduler.resumeJob(jobKey)
    println("Job $jobKey.name resumed.")
} finally {
    scheduler.shutdown()
}