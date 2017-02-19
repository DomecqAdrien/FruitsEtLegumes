package kagura.project.com.a8;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class LoadJson {

    public String loadJSONFromAsset(Context c, String file) {
        String json;
        try {
            InputStream is = c.getAssets().open(file + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];

            //noinspection ResultOfMethodCallIgnored
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
