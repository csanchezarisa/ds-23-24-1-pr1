package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;

import java.util.Date;


public interface ShippingLine {

    int MAX_NUM_SHIPS = 25;
    int MAX_NUM_ROUTES = 15;
    int MAX_CLIENTS = 800;

    int MAX_PEOPLE_CABIN2 = 2;
    int MAX_PEOPLE_CABIN4 = 4;


    enum AccommodationType {
        ARMCHAIR,
        CABIN2,
        CABIN4
    }

    void addShip(String id, String name, int nArmChairs, int nCabins2, int nCabins4, int nParkingLots, int
            unLoadTimeinMinutes);

    void addRoute(String id, String beginningPort, String arrivalPort);

    void addClient(String id, String name, String surname);

    void addVoyage(String id, Date departureDt, Date arrivalDt, String idShip, String idRoute) throws ShipNotFoundException, RouteNotFoundException;

    void reserve(String[] clients, String idVoyage, AccommodationType accommodationType, String idVehicle, double price)
            throws ClientNotFoundException, VoyageNotFoundException, ParkingFullException,
            NoAcommodationAvailableException, MaxExceededException, ReservationAlreadyExistsException;

    void load(String idClient, String idVoyage, Date dt) throws
            LoadingAlreadyException, ClientNotFoundException, VoyageNotFoundException, ReservationNotFoundException;

    Iterator<Reservation> unload(String idVoyage) throws VoyageNotFoundException;

    int unloadTime(String idVehicle, String idVoyage) throws LandingNotDoneException, VoyageNotFoundException, VehicleNotFoundException;

    Iterator<Reservation> getClientReservations(String idClient) throws NoReservationException;

    Iterator<Reservation> getVoyageReservations(String idVoyage) throws NoReservationException;

    Iterator<Reservation> getAccommodationReservations(String idVoyage, AccommodationType accommodationType) throws NoReservationException;

    Client getMostTravelerClient() throws NoClientException;

    Route getMostTraveledRoute() throws NoRouteException;
    /***********************************************************************************/
    /******************** AUX OPERATIONS  **********************************************/
    /***********************************************************************************/


    Ship getShip(String id);

    Route getRoute(String idRoute);

    Client getClient(String id);

    Voyage getVoyage(String id);

    int numShips();

    int numRoutes();

    int numClients();

    int numVoyages();
}


