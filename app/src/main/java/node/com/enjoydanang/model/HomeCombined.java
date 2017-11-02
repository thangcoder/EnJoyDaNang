package node.com.enjoydanang.model;

import node.com.enjoydanang.api.model.Repository;

/**
 * Author: Tavv
 * Created on 02/11/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class HomeCombined {
    Repository<Partner> partnerRepository;
    Repository<Banner> bannerRepository;
    Repository<Category> categoryRepository;

    public HomeCombined(Repository<Partner> partnerRepository, Repository<Banner> bannerRepository, Repository<Category> categoryRepository) {
        this.partnerRepository = partnerRepository;
        this.bannerRepository = bannerRepository;
        this.categoryRepository = categoryRepository;
    }


    public Repository<Partner> getPartnerRepository() {
        return partnerRepository;
    }

    public void setPartnerRepository(Repository<Partner> partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    public Repository<Banner> getBannerRepository() {
        return bannerRepository;
    }

    public void setBannerRepository(Repository<Banner> bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    public Repository<Category> getCategoryRepository() {
        return categoryRepository;
    }

    public void setCategoryRepository(Repository<Category> categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
}
