package library.serviceAPI.loader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tam Huynh on 1/19/2015.
 */
public class GetHeader {
    public Map<String, String> GetStringMap() {
        Map<String, String>  params = new HashMap<String, String>();
        params.put("User-Agent", "Nintendo Gameboy");
        params.put("Accept-Language", "fr");

        return params;
        }
    }
