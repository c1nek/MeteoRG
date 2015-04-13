package com.example.marcin.MeteoRG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Marcin on 2015-03-28.
 */
public class forecast_weather extends Fragment {

    View mainView;
    weather WeatherObjectFragment3;

    RelativeLayout forecastFiled;
    //day1
    TextView d1, max1, min1;
    ImageView ico1;
    //day2
    TextView d2, max2, min2;
    ImageView ico2;

    //day3
    TextView d3, max3, min3;
    ImageView ico3;

    //day4
    TextView d4, max4, min4;
    ImageView ico4;

    //day5
    TextView d5, max5, min5;
    ImageView ico5;

    //day4
    TextView d6, max6, min6;
    ImageView ico6;

    //day7
    TextView d7, max7, min7;
    ImageView ico7;

    //day8
    TextView d8, max8, min8;
    ImageView ico8;

    //day9
    TextView d9, max9, min9;
    ImageView ico9;

    //day10
    TextView d10, max10, min10;
    ImageView ico10;


    List<forecastDay> listDay = new ArrayList<forecastDay>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.forecast_weather, container, false);


        d1 = (TextView) mainView.findViewById(R.id.d1);
        max1 = (TextView) mainView.findViewById(R.id.max1);
        min1 = (TextView) mainView.findViewById(R.id.min1);
        ico1 = (ImageView) mainView.findViewById(R.id.ico1);

        d2 = (TextView) mainView.findViewById(R.id.textView4);
        max2 = (TextView) mainView.findViewById(R.id.textView6);
        min2 = (TextView) mainView.findViewById(R.id.textView5);
        ico2 = (ImageView) mainView.findViewById(R.id.imageView9);

        d3 = (TextView) mainView.findViewById(R.id.textView7);
        max3 = (TextView) mainView.findViewById(R.id.textView9);
        min3 = (TextView) mainView.findViewById(R.id.textView8);
        ico3 = (ImageView) mainView.findViewById(R.id.imageView10);

        d4 = (TextView) mainView.findViewById(R.id.textView10);
        max4 = (TextView) mainView.findViewById(R.id.textView12);
        min4 = (TextView) mainView.findViewById(R.id.textView11);
        ico4 = (ImageView) mainView.findViewById(R.id.imageView11);

        d5 = (TextView) mainView.findViewById(R.id.textView13);
        max5 = (TextView) mainView.findViewById(R.id.textView15);
        min5 = (TextView) mainView.findViewById(R.id.textView14);
        ico5 = (ImageView) mainView.findViewById(R.id.imageView12);

        d6 = (TextView) mainView.findViewById(R.id.textView16);
        max6 = (TextView) mainView.findViewById(R.id.textView18);
        min6 = (TextView) mainView.findViewById(R.id.textView17);
        ico6 = (ImageView) mainView.findViewById(R.id.imageView13);

        d7 = (TextView) mainView.findViewById(R.id.textView19);
        max7 = (TextView) mainView.findViewById(R.id.textView21);
        min7 = (TextView) mainView.findViewById(R.id.textView20);
        ico7 = (ImageView) mainView.findViewById(R.id.imageView14);

        d8 = (TextView) mainView.findViewById(R.id.textView22);
        max8 = (TextView) mainView.findViewById(R.id.textView24);
        min8 = (TextView) mainView.findViewById(R.id.textView23);
        ico8 = (ImageView) mainView.findViewById(R.id.imageView15);

        d9 = (TextView) mainView.findViewById(R.id.textView25);
        max9 = (TextView) mainView.findViewById(R.id.textView27);
        min9 = (TextView) mainView.findViewById(R.id.textView26);
        ico9 = (ImageView) mainView.findViewById(R.id.imageView16);

        d10 = (TextView) mainView.findViewById(R.id.textView28);
        max10 = (TextView) mainView.findViewById(R.id.textView30);
        min10 = (TextView) mainView.findViewById(R.id.textView29);
        ico10 = (ImageView) mainView.findViewById(R.id.imageView17);
        //forecastFiled = (RelativeLayout) mainView.findViewById(R.id.layoutField);


        WeatherObjectFragment3 = ((MainActivity) getActivity()).getwWather();
        listDay = WeatherObjectFragment3.listDay;
        fillWithData();

        return mainView;
    }


    public void fillWithData() {
        WeatherObjectFragment3 = ((MainActivity) getActivity()).getwWather();
        listDay = WeatherObjectFragment3.listDay;
        try {

            d1.setText(listDay.get(0).getDay());
            max1.setText(Integer.toString(listDay.get(0).getTempMax()) + "\u00b0" + "C");
            min1.setText(Integer.toString(listDay.get(0).getTempMin()) + "\u00b0" + "C");
            new Thread(LoadPhotoFromURLd1).start();

            d2.setText(listDay.get(1).getDay());
            max2.setText(Integer.toString(listDay.get(1).getTempMax()) + "\u00b0" + "C");
            min2.setText(Integer.toString(listDay.get(1).getTempMin()) + "\u00b0" + "C");
            new Thread(LoadPhotoFromURLd2).start();

            d3.setText(listDay.get(2).getDay());
            max3.setText(Integer.toString(listDay.get(2).getTempMax()) + "\u00b0" + "C");
            min3.setText(Integer.toString(listDay.get(2).getTempMin()) + "\u00b0" + "C");
            new Thread(LoadPhotoFromURLd3).start();

            d4.setText(listDay.get(3).getDay());
            max4.setText(Integer.toString(listDay.get(3).getTempMax()) + "\u00b0" + "C");
            min4.setText(Integer.toString(listDay.get(3).getTempMin()) + "\u00b0" + "C");
            new Thread(LoadPhotoFromURLd4).start();

            d5.setText(listDay.get(4).getDay());
            max5.setText(Integer.toString(listDay.get(4).getTempMax()) + "\u00b0" + "C");
            min5.setText(Integer.toString(listDay.get(4).getTempMin()) + "\u00b0" + "C");
            new Thread(LoadPhotoFromURLd5).start();

            d6.setText(listDay.get(5).getDay());
            max6.setText(Integer.toString(listDay.get(5).getTempMax()) + "\u00b0" + "C");
            min6.setText(Integer.toString(listDay.get(5).getTempMin()) + "\u00b0" + "C");
            new Thread(LoadPhotoFromURLd6).start();

            d7.setText(listDay.get(6).getDay());
            max7.setText(Integer.toString(listDay.get(6).getTempMax()) + "\u00b0" + "C");
            min7.setText(Integer.toString(listDay.get(6).getTempMin()) + "\u00b0" + "C");
            new Thread(LoadPhotoFromURLd7).start();

            d8.setText(listDay.get(7).getDay());
            max8.setText(Integer.toString(listDay.get(7).getTempMax()) + "\u00b0" + "C");
            min8.setText(Integer.toString(listDay.get(7).getTempMin()) + "\u00b0" + "C");
            new Thread(LoadPhotoFromURLd8).start();

            d9.setText(listDay.get(8).getDay());
            max9.setText(Integer.toString(listDay.get(8).getTempMax()) + "\u00b0" + "C");
            min9.setText(Integer.toString(listDay.get(8).getTempMin()) + "\u00b0" + "C");
            new Thread(LoadPhotoFromURLd9).start();

            d10.setText(listDay.get(9).getDay());
            max10.setText(Integer.toString(listDay.get(9).getTempMax()) + "\u00b0" + "C");
            min10.setText(Integer.toString(listDay.get(9).getTempMin()) + "\u00b0" + "C");
            new Thread(LoadPhotoFromURLd10).start();
        } catch (Exception ex) {

        }
    }



    public Runnable LoadPhotoFromURLd1 = new Runnable() {


        public void run() {
            try {
                URL Url = new URL(listDay.get(0).getIcoURL());
                final Bitmap tempImage = BitmapFactory.decodeStream(Url.openConnection().getInputStream());
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        ico1.setImageBitmap(createTransparentBitmapFromBitmap(tempImage));
                    }
                });
            } catch (MalformedURLException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public Runnable LoadPhotoFromURLd2 = new Runnable() {


        public void run() {
            try {
                URL Url = new URL(listDay.get(1).getIcoURL());
                final Bitmap tempImage = BitmapFactory.decodeStream(Url.openConnection().getInputStream());
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        ico2.setImageBitmap(createTransparentBitmapFromBitmap(tempImage));
                    }
                });
            } catch (MalformedURLException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public Runnable LoadPhotoFromURLd3 = new Runnable() {


        public void run() {
            try {
                URL Url = new URL(listDay.get(2).getIcoURL());
                final Bitmap tempImage = BitmapFactory.decodeStream(Url.openConnection().getInputStream());
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        ico3.setImageBitmap(createTransparentBitmapFromBitmap(tempImage));
                    }
                });
            } catch (MalformedURLException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public Runnable LoadPhotoFromURLd4 = new Runnable() {


        public void run() {
            try {
                URL Url = new URL(listDay.get(3).getIcoURL());
                final Bitmap tempImage = BitmapFactory.decodeStream(Url.openConnection().getInputStream());
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        ico4.setImageBitmap(createTransparentBitmapFromBitmap(tempImage));
                    }
                });
            } catch (MalformedURLException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public Runnable LoadPhotoFromURLd5 = new Runnable() {


        public void run() {
            try {
                URL Url = new URL(listDay.get(4).getIcoURL());
                final Bitmap tempImage = BitmapFactory.decodeStream(Url.openConnection().getInputStream());
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        ico5.setImageBitmap(createTransparentBitmapFromBitmap(tempImage));
                    }
                });
            } catch (MalformedURLException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public Runnable LoadPhotoFromURLd6 = new Runnable() {


        public void run() {
            try {
                URL Url = new URL(listDay.get(5).getIcoURL());
                final Bitmap tempImage = BitmapFactory.decodeStream(Url.openConnection().getInputStream());
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        ico6.setImageBitmap(createTransparentBitmapFromBitmap(tempImage));
                    }
                });
            } catch (MalformedURLException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public Runnable LoadPhotoFromURLd7 = new Runnable() {


        public void run() {
            try {
                URL Url = new URL(listDay.get(6).getIcoURL());
                final Bitmap tempImage = BitmapFactory.decodeStream(Url.openConnection().getInputStream());
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        ico7.setImageBitmap(createTransparentBitmapFromBitmap(tempImage));
                    }
                });
            } catch (MalformedURLException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public Runnable LoadPhotoFromURLd8 = new Runnable() {


        public void run() {
            try {
                URL Url = new URL(listDay.get(7).getIcoURL());
                final Bitmap tempImage = BitmapFactory.decodeStream(Url.openConnection().getInputStream());
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        ico8.setImageBitmap(createTransparentBitmapFromBitmap(tempImage));
                    }
                });
            } catch (MalformedURLException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public Runnable LoadPhotoFromURLd9 = new Runnable() {


        public void run() {
            try {
                URL Url = new URL(listDay.get(8).getIcoURL());
                final Bitmap tempImage = BitmapFactory.decodeStream(Url.openConnection().getInputStream());
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        ico9.setImageBitmap(createTransparentBitmapFromBitmap(tempImage));
                    }
                });
            } catch (MalformedURLException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public Runnable LoadPhotoFromURLd10 = new Runnable() {


        public void run() {
            try {
                URL Url = new URL(listDay.get(9).getIcoURL());
                final Bitmap tempImage = BitmapFactory.decodeStream(Url.openConnection().getInputStream());
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        ico10.setImageBitmap(createTransparentBitmapFromBitmap(tempImage));
                    }
                });
            } catch (MalformedURLException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    Bitmap replaceWhireToTransparent(Bitmap myBitmap) {

        int[] allpixels = new int[myBitmap.getHeight() * myBitmap.getWidth()];

        myBitmap.getPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());

        for (int i = 0; i < myBitmap.getHeight() * myBitmap.getWidth(); i++) {

            if (allpixels[i] == Color.WHITE)
                allpixels[i] = Color.TRANSPARENT;
        }

        myBitmap.setPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());

        return myBitmap;
    }

    public static Bitmap createTransparentBitmapFromBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            int picw = bitmap.getWidth();
            int pich = bitmap.getHeight();
            int[] pix = new int[picw * pich];
            bitmap.getPixels(pix, 0, picw, 0, 0, picw, pich);

            for (int y = 0; y < pich; y++) {
                // from left to right
                for (int x = 0; x < picw; x++) {
                    int index = y * picw + x;
                    int r = (pix[index] >> 16) & 0xff;
                    int g = (pix[index] >> 8) & 0xff;
                    int b = pix[index] & 0xff;

                    if (pix[index] == Color.WHITE) {
                        pix[index] = Color.TRANSPARENT;
                    } else {
                        break;
                    }
                }

                // from right to left
                for (int x = picw - 1; x >= 0; x--) {
                    int index = y * picw + x;
                    int r = (pix[index] >> 16) & 0xff;
                    int g = (pix[index] >> 8) & 0xff;
                    int b = pix[index] & 0xff;

                    if (pix[index] == Color.WHITE) {
                        pix[index] = Color.TRANSPARENT;
                    } else {
                        break;
                    }
                }
            }

            Bitmap bm = Bitmap.createBitmap(pix, picw, pich,
                    Bitmap.Config.ARGB_4444);

            return bm;
        }

        return null;

}}

