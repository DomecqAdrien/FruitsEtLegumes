package kagura.project.com.a8;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;



public class AssociationPicturale extends Association {

    public AssociationPicturale(Context context) {
        super(context);
    }

    @Override
    public  List<Integer[]> loadCards(){
        List<Integer> imagesId = loadImages();
        List<Integer[]> idDrawablesFrontAndBack = new ArrayList<>();

        Integer[] mThumbIdsFront = new Integer[size];
        Integer[] mThumbIdsBack = new Integer[size];

        Log.i("loadCards()","size=" + size);

        imagePositions = new ArrayList<>(Collections.nCopies(size, 0));
        Log.i("imagePositionInit", imagePositions.toString());
        List<Integer> listIntegers = new ArrayList<>();

        for(int i = 0; i < size; i++) {
            listIntegers.add(i);
        }

        Random r = new Random();

        for(int i = 0; i < (size / 2); i++){
            int randomPositionCard1;
            int randomPositionCard2;
            int randomImage = r.nextInt(imagesId.size());

            Log.i("k","k");
            if(!imagePositions.contains(imagesId.get(randomImage))){
                randomPositionCard1 = r.nextInt(listIntegers.size());
                imagePositions.set(listIntegers.get(randomPositionCard1), imagesId.get(randomImage));

                Log.i("carte 1 :", "Position " + listIntegers.get(randomPositionCard1));
                listIntegers.remove(randomPositionCard1);



                randomPositionCard2 = r.nextInt(listIntegers.size());
                imagePositions.set(listIntegers.get(randomPositionCard2), imagesId.get(randomImage));

                Log.i("carte 2 :", "Position " + listIntegers.get(randomPositionCard2));
                listIntegers.remove(randomPositionCard2);

            }
        }
        Log.i("imagePositionsAfter", imagePositions.toString());

        int backImage = context.getResources().getIdentifier("test", "drawable", context.getPackageName());
        for(int i = 0; i < imagePositions.size(); i++){
            mThumbIdsFront[i] = backImage;
            mThumbIdsBack[i] = imagePositions.get(i);
        }

        idDrawablesFrontAndBack.add(0, mThumbIdsFront);
        idDrawablesFrontAndBack.add(1, mThumbIdsBack);

        return idDrawablesFrontAndBack;



    }

    @Override
    public List<Integer> loadImages(){

        List<String> nameImages = new ArrayList<>();
        List<Integer> idImages = new ArrayList<>();

        Field[] fields = R.drawable.class.getDeclaredFields();
        int i = 0;
        for (Field field : fields) {
            if (field.getName().startsWith("legume")) {
                nameImages.add(field.getName());
                Log.i("name", nameImages.get(i));
                i++;
            }
        }

        for (int j = 0; j < nameImages.size(); j++){
            int cardId = context.getResources().getIdentifier(nameImages.get(j), "drawable", context.getPackageName());
            idImages.add(cardId);
        }
        Log.i("nameImages", nameImages.toString());
        Log.i("images ID", idImages.toString());

        return idImages;
    }


}