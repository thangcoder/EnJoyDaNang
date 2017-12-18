package node.com.enjoydanang.model;

import node.com.enjoydanang.api.model.Repository;

/**
 * Author: Tavv
 * Created on 02/11/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class DetailPartnerCombined {
    Repository<DetailPartner> detailPartnerRepository;
    Repository<PartnerAlbum> partnerAlbumRepository;
    Repository<Partner> partnerRepository;

    public DetailPartnerCombined(Repository<DetailPartner> detailPartnerRepository, Repository<PartnerAlbum> partnerAlbumRepository) {
        this.detailPartnerRepository = detailPartnerRepository;
        this.partnerAlbumRepository = partnerAlbumRepository;
    }

    public DetailPartnerCombined(Repository<DetailPartner> detailPartnerRepository,
                                 Repository<PartnerAlbum> partnerAlbumRepository,
                                 Repository<Partner> partnerRepository) {
        this.detailPartnerRepository = detailPartnerRepository;
        this.partnerAlbumRepository = partnerAlbumRepository;
        this.partnerRepository = partnerRepository;
    }

    public Repository<DetailPartner> getDetailPartnerRepository() {
        return detailPartnerRepository;
    }

    public void setDetailPartnerRepository(Repository<DetailPartner> detailPartnerRepository) {
        this.detailPartnerRepository = detailPartnerRepository;
    }

    public Repository<PartnerAlbum> getPartnerAlbumRepository() {
        return partnerAlbumRepository;
    }

    public void setPartnerAlbumRepository(Repository<PartnerAlbum> partnerAlbumRepository) {
        this.partnerAlbumRepository = partnerAlbumRepository;
    }

    public Repository<Partner> getPartnerRepository() {
        return partnerRepository;
    }

    public void setPartnerRepository(Repository<Partner> partnerRepository) {
        this.partnerRepository = partnerRepository;
    }
}
