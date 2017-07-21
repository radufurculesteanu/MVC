package ro.teamnet.zth.appl.service;

import ro.teamnet.zth.appl.dao.LocationDao;
import ro.teamnet.zth.appl.domain.Location;

import java.util.List;

/**
 * Created by Radu.Furculesteanu on 7/21/2017.
 */
public class LocationServiceImpl implements LocationService{
    LocationDao locationDao = new LocationDao();

    @Override
    public List<Location> findAll() {
        return locationDao.getAllLocations();
    }

    @Override
    public Location findOne(Long locationId) {
        return locationDao.getLocationById(locationId);
    }
}
