package cn.leo.crawler4j.crawler;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import cn.leo.cl.service.CLService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

@Component
public class Web1024Crawler extends WebCrawler {
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp3|zip|gz))$");
    
    @Autowired
    private CLService service; 
    
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches()
               && href.contains("cl.tarinx.com/");
    }
    
    @Override
    public void visit(Page page) {
        if(service==null){
            init();
        }
        
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            service.convertHtml(page.getWebURL().getURL(), htmlParseData.getHtml());
        }
    }

    private void init()  {
      @SuppressWarnings("resource")
      ApplicationContext context = new ClassPathXmlApplicationContext(
              "classpath:spring/config-app.xml");
      service=context.getBean(CLService.class);
  }
}
