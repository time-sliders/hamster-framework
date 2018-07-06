import com.alibaba.fastjson.JSON;

/**
 * @author LuYun
 * @version app 7.0.0
 * @since 2018.06.05
 */
public class MainTest {

    public static void main(String[] args) {
        String[][][] a = new String[1][1][1];
        a[0][0][0] = "zhw";
        System.out.println(JSON.toJSONString(a));

        String[][][] s = (String[][][])JSON.parse("[[[\"zhw\"]]]");
        System.out.println(s[0][0][0]);
    }
}
