package webapp.demoa_interceptor;

import org.noear.solon.annotation.XBefore;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;

@XBefore({BeforeHandler.class})
@XController
public class TestController {
    @XMapping("/demoa/test/")
    public void test(){

    }
}
