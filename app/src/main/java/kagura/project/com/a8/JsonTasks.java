package kagura.project.com.a8;


import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import kagura.project.com.a8.objects.Result;

public class JsonTasks {

    Result result;


    public List<Result> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readJsonArray(reader);
        } finally {
            reader.close();
        }
    }

    private List<Result> readJsonArray(JsonReader reader) throws IOException {

            List<Result> results = new ArrayList<>();

            reader.beginObject();
            reader.nextName();
            reader.beginArray();
            while(reader.hasNext()){
                results.add(readMessage(reader));
            }
            reader.endArray();
            reader.close();
            Log.i("results", results.toString());
            return results;
    }

    private Result readMessage(JsonReader reader) throws IOException{
        Result result = new Result();

        reader.beginObject();
        while (reader.hasNext()) {

            String type = reader.nextName();

            switch (type) {
                case "name":
                    result.setName(reader.nextString());
                    break;

                case "game":
                    result.setGame(reader.nextString());
                    break;

                case "level":
                    result.setLevel(reader.nextInt());
                    break;

                case "tries":
                    result.setTries(reader.nextInt());
                    break;

                case "time":
                    result.setTime(reader.nextLong());
                    break;

                default:
                    reader.skipValue(); //avoid some unhandle events
                    break;
            }
        }
        reader.endObject();
        return result;
    }

    public void writeJsonStream(OutputStream out, List<Result> result) throws IOException{
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");
        writeMessagesArray(writer, result);
        writer.flush();
        writer.close();

    }

    private void writeMessagesArray(JsonWriter writer, List<Result> results) throws IOException {
        writer.beginObject();
        writer.name("resultats");
        writer.beginArray();

        for(int i =0; i < results.size(); i++){
            result = results.get(i);
            writeMessage(writer, result);
        }
        writer.endArray();
        writer.endObject();

    }

    private void writeMessage(JsonWriter writer, Result result) throws IOException {
        writer.beginObject();
        writer.name("name").value(result.getName());
        writer.name("game").value(result.getGame());
        writer.name("level").value(result.getLevel());
        writer.name("tries").value(result.getTries());
        writer.name("time").value(result.getTime());
        writer.endObject();
    }


}
