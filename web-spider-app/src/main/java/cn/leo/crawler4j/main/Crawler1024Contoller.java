package cn.leo.crawler4j.main;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import cn.leo.crawler4j.crawler.Web1024Crawler;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

@Slf4j
@Component
public class Crawler1024Contoller {

    public static void main(String[] args) {
        @SuppressWarnings("resource")
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:spring/config-app.xml");
        
        Crawler1024Contoller controller=context.getBean(Crawler1024Contoller.class);
        controller.start();
    }
    
    public void start(){
        log.debug("start craw 1024...");
        
        String crawlStorageFolder = "/aliyun/crawl/root";
        int numberOfCrawlers = 7;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setPolitenessDelay(1000);
        config.setUserAgentString("Leo");

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        //不遵守协议
        robotstxtConfig.setEnabled(false);
        
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,
                pageFetcher);
        CrawlController controller;
        try {
            controller = new CrawlController(config, pageFetcher,
                    robotstxtServer);
            /*
             * For each crawl, you need to add some seed urls. These are the first
             * URLs that are fetched and then the crawler starts following links
             * which are found in these pages
             */
            controller.addSeed("http://cl.tarinx.com/");
            
            /*
             * Start the crawl. This is a blocking operation, meaning that your code
             * will reach the line after this only when crawling is finished.
             */
            controller.start(Web1024Crawler.class, numberOfCrawlers);
        } catch (Exception e) {
            log.error("",e);
        }
    }
}
