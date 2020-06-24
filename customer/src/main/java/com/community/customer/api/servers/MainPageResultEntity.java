package com.community.customer.api.servers;

import com.community.support.common.BaseResult;

import java.io.Serializable;
import java.util.ArrayList;

public class MainPageResultEntity extends BaseResult<MainPageResultEntity> implements Serializable {
    private ArrayList<CarouselEntity> carousels;
    private ArrayList<ServiceBlockEntity> fiveClassify;
    private ArrayList<ServiceBlockEntity> tenServer;
    private ArrayList<ServiceBlockEntity> recommendServer;
    private ArrayList<ServerClassify> threeServers;

    public ArrayList<CarouselEntity> getCarousels() {
        return carousels;
    }

    public void setCarousels(ArrayList<CarouselEntity> carousels) {
        this.carousels = carousels;
    }

    public ArrayList<ServiceBlockEntity> getFiveClassify() {
        return fiveClassify;
    }

    public void setFiveClassify(ArrayList<ServiceBlockEntity> fiveClassify) {
        this.fiveClassify = fiveClassify;
    }

    public ArrayList<ServiceBlockEntity> getTenServer() {
        return tenServer;
    }

    public void setTenServer(ArrayList<ServiceBlockEntity> tenServer) {
        this.tenServer = tenServer;
    }

    public ArrayList<ServerClassify> getThreeServers() {
        return threeServers;
    }

    public void setThreeServers(ArrayList<ServerClassify> threeServers) {
        this.threeServers = threeServers;
    }

    public ArrayList<ServiceBlockEntity> getRecommendServer() {
        return recommendServer;
    }

    public void setRecommendServer(ArrayList<ServiceBlockEntity> recommendServer) {
        this.recommendServer = recommendServer;
    }
}
