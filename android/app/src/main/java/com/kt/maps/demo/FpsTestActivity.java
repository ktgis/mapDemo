package com.kt.maps.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kt.maps.GMap;
import com.kt.maps.GMap.OnIdleListener;
import com.kt.maps.GMap.OnViewpointChangeListener;
import com.kt.maps.GMapFragment;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.ViewpointChange;
import com.kt.maps.internal.RenderScheduler.OnFpsListener;
import com.kt.maps.internal.util.Logger;
import com.kt.geom.model.UTMK;
import com.kt.maps.model.Viewpoint;
import com.kt.maps.util.GMapKeyManager;

import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FpsTestActivity extends Activity implements OnMapReadyListener, OnFpsListener,
        OnViewpointChangeListener, OnIdleListener {

    private UTMK 김포 = new UTMK(930768, 1957604);
    private UTMK 김포공항 = new UTMK(937141, 1953991);
    private UTMK 성남 = new UTMK(969212, 1936706);
    private UTMK 개포 = new UTMK(960195, 1941818);
    private UTMK 상봉역 = new UTMK(963430, 1955216);
    private UTMK 광명시청 = new UTMK(943842, 1942397);
    
    private UTMK 레벨3_1위 = new UTMK(945658 ,1938925);
    private UTMK 레벨3_2위 = new UTMK(994458 ,1816893);
    
    private UTMK 레벨4_1위 = new UTMK(960666, 1955133);
    private UTMK 레벨4_2위 = new UTMK(948378, 1942845);
    private UTMK 레벨5_1위 = new UTMK(1141146, 1677885);
    private UTMK 레벨5_2위 = new UTMK(1141146, 1684029);

    private final LinkedHashMap<String, Runnable> ITEMS = new LinkedHashMap<String, Runnable>() {
        {
            put("spin!", new Runnable() {
                public void run() {
                    schedule(new Runnable() {
                        public void run() {
                            measureFps(10000);
                            gMap.animate(ViewpointChange.rotateBy(1800), 10000);
                        }
                    }, 500);
                }
             });
            put("교통정보", new Runnable() {
               public void run() {
                 gMap.setNetworkLayerVisible(!gMap.isNetworkLayerVisible());
               }
            });
            put("레벨3-1(6)", new Runnable() {
                public void run() {
                    gMap.change(ViewpointChange.moveTo(레벨3_1위, 6, 0, 0));
                    schedule(new Runnable() {
                        public void run() {
                            measureFps(10000);
                            gMap.animate(ViewpointChange.rotateBy(1800), 10000);
                        }
                    }, 500);
                }
             });
             put("레벨3-1(7)", new Runnable() {
                 public void run() {
                     gMap.change(ViewpointChange.moveTo(레벨3_1위, 7, 0, 0));
                     schedule(new Runnable() {
                         public void run() {
                             measureFps(10000);
                             gMap.animate(ViewpointChange.rotateBy(1800), 10000);
                         }
                     }, 500);
                 }
              });
             put("레벨3-2(6)", new Runnable() {
                 public void run() {
                     gMap.change(ViewpointChange.moveTo(레벨3_2위, 6, 0, 0));
                     schedule(new Runnable() {
                         public void run() {
                             measureFps(10000);
                             gMap.animate(ViewpointChange.rotateBy(1800), 10000);
                         }
                     }, 500);
                 }
              });
              put("레벨3-2(7)", new Runnable() {
                  public void run() {
                      gMap.change(ViewpointChange.moveTo(레벨3_2위, 7, 0, 0));
                      schedule(new Runnable() {
                          public void run() {
                              measureFps(10000);
                              gMap.animate(ViewpointChange.rotateBy(1800), 10000);
                          }
                      }, 500);
                  }
               });
            put("레벨4-1(8)", new Runnable() {
               public void run() {
                   gMap.change(ViewpointChange.moveTo(레벨4_1위, 8, 0, 0));
                   schedule(new Runnable() {
                       public void run() {
                           measureFps(10000);
                           gMap.animate(ViewpointChange.rotateBy(1800), 10000);
                       }
                   }, 500);
               }
            });
            put("레벨4-1(9)", new Runnable() {
                public void run() {
                    gMap.change(ViewpointChange.moveTo(레벨4_1위, 9, 0, 0));
                    schedule(new Runnable() {
                        public void run() {
                            measureFps(10000);
                            gMap.animate(ViewpointChange.rotateBy(1800), 10000);
                        }
                    }, 500);
                }
             });
            put("레벨4-2(8)", new Runnable() {
                public void run() {
                    gMap.change(ViewpointChange.moveTo(레벨4_2위, 8, 0, 0));
                    schedule(new Runnable() {
                        public void run() {
                            measureFps(10000);
                            gMap.animate(ViewpointChange.rotateBy(1800), 10000);
                        }
                    }, 500);
                }
             });
             put("레벨4-2(9)", new Runnable() {
                 public void run() {
                     gMap.change(ViewpointChange.moveTo(레벨4_2위, 9, 0, 0));
                     schedule(new Runnable() {
                         public void run() {
                             measureFps(10000);
                             gMap.animate(ViewpointChange.rotateBy(1800), 10000);
                         }
                     }, 500);
                 }
              });
             put("레벨5-1(10)", new Runnable() {
                 public void run() {
                     gMap.change(ViewpointChange.moveTo(레벨5_1위, 10, 0, 0));
                     schedule(new Runnable() {
                         public void run() {
                             measureFps(10000);
                             gMap.animate(ViewpointChange.rotateBy(1800), 10000);
                         }
                     }, 500);
                 }
             });
             put("레벨5-1(11)", new Runnable() {
                 public void run() {
                     gMap.change(ViewpointChange.moveTo(레벨5_1위, 11, 0, 0));
                     schedule(new Runnable() {
                         public void run() {
                             measureFps(10000);
                             gMap.animate(ViewpointChange.rotateBy(1800), 10000);
                         }
                     }, 500);
                 }
             });
             
             put("레벨5-2(10)", new Runnable() {
                 public void run() {
                     gMap.change(ViewpointChange.moveTo(레벨5_2위, 10, 0, 0));
                     schedule(new Runnable() {
                         public void run() {
                             measureFps(10000);
                             gMap.animate(ViewpointChange.rotateBy(1800), 10000);
                         }
                     }, 500);
                 }
             });
             put("레벨5-2(11)", new Runnable() {
                 public void run() {
                     gMap.change(ViewpointChange.moveTo(레벨5_2위, 11, 0, 0));
                     schedule(new Runnable() {
                         public void run() {
                             measureFps(10000);
                             gMap.animate(ViewpointChange.rotateBy(1800), 10000);
                         }
                     }, 500);
                 }
             });
             put("레벨5-2(12)", new Runnable() {
                 public void run() {
                     gMap.change(ViewpointChange.moveTo(레벨5_2위, 12, 0, 0));
                     schedule(new Runnable() {
                         public void run() {
                             measureFps(10000);
                             gMap.animate(ViewpointChange.rotateBy(1800), 10000);
                         }
                     }, 500);
                 }
             });
             put("레벨5-2(13)", new Runnable() {
                 public void run() {
                     gMap.change(ViewpointChange.moveTo(레벨5_2위, 13, 0, 0));
                     schedule(new Runnable() {
                         public void run() {
                             measureFps(10000);
                             gMap.animate(ViewpointChange.rotateBy(1800), 10000);
                         }
                     }, 500);
                 }
             });
            put("상봉-광명(zoom:7)", new Runnable() {
                @Override
                public void run() {
                    gMap.change(ViewpointChange.moveTo(상봉역, 7, 0, 0));
                    schedule(new Runnable() {

                        @Override
                        public void run() {
                            measureFps(10000);
                            gMap.animate(ViewpointChange.panTo(광명시청), 10000);
                        }
                    }, 500);
                }
            });
            put("상봉-광명(zoom:8)", new Runnable() {
                @Override
                public void run() {
                    gMap.change(ViewpointChange.moveTo(상봉역, 8, 0, 0));
                    schedule(new Runnable() {

                        @Override
                        public void run() {
                            measureFps(10000);
                            gMap.animate(ViewpointChange.panTo(광명시청), 10000);
                        }
                    }, 500);
                }
            });
            put("상봉-광명(zoom:9)", new Runnable() {
                @Override
                public void run() {
                    gMap.change(ViewpointChange.moveTo(상봉역, 9, 0, 0));
                    schedule(new Runnable() {

                        @Override
                        public void run() {
                            measureFps(20000);
                            gMap.animate(ViewpointChange.panTo(광명시청), 20000);
                        }
                    }, 500);
                }
            });
            put("panning (zoom:7)", new Runnable() {
                @Override
                public void run() {
                    gMap.change(ViewpointChange.moveTo(김포, 7, 20, 0));
                    schedule(new Runnable() {

                        @Override
                        public void run() {
                            measureFps(10000);
                            gMap.animate(ViewpointChange.panTo(성남), 10000);
                        }
                    }, 500);
                }
            });
            put("panning (zoom:8)", new Runnable() {
                @Override
                public void run() {
                    gMap.change(ViewpointChange.moveTo(김포, 8, 20, 0));
                    schedule(new Runnable() {

                        @Override
                        public void run() {
                            measureFps(10000);
                            gMap.animate(ViewpointChange.panTo(성남), 10000);
                        }
                    }, 500);
                }
            });
            put("panning (zoom:9)", new Runnable() {
                @Override
                public void run() {
                    gMap.change(ViewpointChange.moveTo(김포, 9, 20, 0));
                    schedule(new Runnable() {

                        @Override
                        public void run() {
                            measureFps(20000);
                            gMap.animate(ViewpointChange.panTo(개포), 20000);
                        }
                    }, 500);
                }
            });
            put("panning (zoom:10)", new Runnable() {
                @Override
                public void run() {
                    gMap.change(ViewpointChange.moveTo(김포공항, 10, 20, 0));
                    schedule(new Runnable() {

                        @Override
                        public void run() {
                            measureFps(50000);
                            gMap.animate(ViewpointChange.panTo(개포), 50000);
                        }
                    }, 500);
                }
            });
            put("panning (zoom:11)", new Runnable() {
                @Override
                public void run() {
                    gMap.change(ViewpointChange.moveTo(김포공항, 11, 20, 0));
                    schedule(new Runnable() {

                        @Override
                        public void run() {
                            measureFps(60000);
                            gMap.animate(ViewpointChange.panTo(개포), 60000);
                        }
                    }, 500);
                }
            });
        }
    };

    private GMap gMap;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ScheduledExecutorService scheduledExecutor;
    private ImageView compassNeedle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fps_test);

        GMapFragment fragment =
                (GMapFragment) getFragmentManager().findFragmentById(R.id.gmap2);
        fragment.setOnMapReadyListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ListView drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item,
                ITEMS.keySet().toArray(new String[0])));
        drawerList.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((Runnable) ITEMS.values().toArray()[position]).run();
                drawerLayout.closeDrawer(drawerList);
            }

        });
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_launcher,
                R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerListener(drawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        compassNeedle = (ImageView) findViewById(R.id.compass_needle);

        compassNeedle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gMap != null) {
                    gMap.animate(ViewpointChange.rotateTo(0));
                }
            }
        });

        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    private void schedule(final Runnable run, long delay) {
        scheduledExecutor.schedule(new Runnable() {

            @Override
            public void run() {
                runOnUiThread(run);
            }
        }, delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GMap gMap) {
        this.gMap = gMap;
        gMap.setOnViewpointChangeListener(this);
        gMap.setOnIdleListener(this);
        gMap.getRenderScheduler().setOnFpsListener(this);
        gMap.setGTrafficLayerAdaptor(new TrafficAdaptor());
//        gMap.getNativeManager().getLayerManager().setFullAnimationUpdate(true);
    }

    public void onZoomIn(View view) {
        if (gMap != null)
            gMap.animate(ViewpointChange.zoomIn());
    }

    public void onZoomOut(View view) {
        if (gMap != null)
            gMap.animate(ViewpointChange.zoomOut());
    }

    @Override
    public void onViewpointChange(GMap map, Viewpoint viewpoint, boolean gesture) {
        compassNeedle.setRotation(-viewpoint.rotation);
        compassNeedle.setRotationX(viewpoint.tilt * 1.5f);
    }

    public void measureFps(long time) {
        Logger.d("FPS", "fps test start -------");
        gMap.getRenderScheduler().startMeasureFps();
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                stopMeasureFps(3000);
            }
        }, time);
    }

    public void stopMeasureFps(final long disappearInfoDelay) {
        final float fps = gMap.getRenderScheduler().stopMeasureFps();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Logger.d("FPS", "fps test end -------");
                final TextView tv = (TextView) findViewById(R.id.fps_info_center);
                tv.setText(String.format("%.1f fps", fps));
                tv.setVisibility(View.VISIBLE);
                new Timer().schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setVisibility(View.INVISIBLE);
                            }

                        });
                    }

                }, disappearInfoDelay);
            }

        });
    }

    @Override
    public void fps(final float fps) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final TextView tv = (TextView) findViewById(R.id.fps_info);
                tv.setText(String.format("%.1ffps", fps));
                Logger.d("FPS", "fps: {}", fps);
            }
        });

    }

    @Override
    public void onIdle(GMap map) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final TextView tv = (TextView) findViewById(R.id.idle_info);
                tv.setVisibility(View.VISIBLE);
                new Timer().schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                tv.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }, 1000);
                
                ((TextView)findViewById(R.id.mapInfo)).setText("zoom: "+ gMap.getViewpoint().zoom);
            }
        });

    }


    @Override
    public void onFailReadyMap(GMapKeyManager.ResultCode code) {
        Toast.makeText(this, "Result Code : " + code.toString(), Toast.LENGTH_SHORT).show();
    }
}
