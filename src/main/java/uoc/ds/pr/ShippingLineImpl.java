package uoc.ds.pr;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.util.DSArray;
import uoc.ds.pr.util.OrderedVector;
import uoc.ds.pr.util.Utils;

import java.util.Comparator;
import java.util.Date;


public class ShippingLineImpl implements ShippingLine {

    private final DSArray<Ship> ships = new DSArray<>();
    private final DSArray<Route> routes = new DSArray<>();
    private final LinkedList<Client> clients = new LinkedList<>();
    private final LinkedList<Voyage> voyages = new LinkedList<>();
    private final OrderedVector<Client> mostTraveledClients = new OrderedVector<>(Comparator.comparingInt(Client::countLoadedReservations));
    private final OrderedVector<Route> mostTraveledRoutes = new OrderedVector<>(Comparator.comparingInt(Route::numVoyages)
            .thenComparing(Route::getId));

    @Override
    public void addShip(String id, String name, int nArmChairs, int nCabins2, int nCabins4, int nParkingLots, int unLoadTimeinMinutes) {
        Ship ship = new Ship(id, name, nArmChairs, nCabins2, nCabins4, nParkingLots, unLoadTimeinMinutes);

        int index = ships.indexOf(ship);
        if (index >= 0) {
            ships.modify(index, ship);
        } else {
            ships.add(new Ship(id, name, nArmChairs, nCabins2, nCabins4, nParkingLots, unLoadTimeinMinutes));
        }
    }

    @Override
    public void addRoute(String id, String beginningPort, String arrivalPort) {
        Route route = new Route(id, beginningPort, arrivalPort);

        int index = routes.indexOf(route);
        if (index >= 0) {
            routes.modify(index, route);
        } else {
            routes.add(new Route(id, beginningPort, arrivalPort));
        }
    }

    @Override
    public void addClient(String id, String name, String surname) {
        Client client = new Client(id, name, surname);

        Utils.findPosition(clients.positions(), client)
                .ifPresentOrElse(
                        p -> clients.update(p, client),
                        () -> clients.insertEnd(client));
    }

    @Override
    public void addVoyage(String id, Date departureDt, Date arrivalDt, String idShip, String idRoute)
            throws ShipNotFoundException, RouteNotFoundException {

        Ship ship = Utils.find(idShip, ships.values())
                .orElseThrow(() -> new ShipNotFoundException(idShip));
        Route route = Utils.find(idRoute, routes.values())
                .orElseThrow(() -> new RouteNotFoundException(idRoute));

        Voyage voyage = new Voyage(id, departureDt, arrivalDt, ship, route);

        Utils.findPosition(voyages.positions(), voyage)
                .ifPresentOrElse(
                        p -> voyages.update(p, voyage),
                        () -> voyages.insertEnd(voyage));

        ship.addVoyage(voyage);
        route.addVoyage(voyage);
        mostTraveledRoutes.update(route);
    }

    @Override
    public void reserve(String[] clients, String idVoyage, AccommodationType accommodationType, String idVehicle, double price) throws ClientNotFoundException, VoyageNotFoundException, ParkingFullException, NoAccommodationAvailableException, MaxExceededException, ReservationAlreadyExistsException {

        // 1. Validate voyage exists
        Voyage voyage = Utils.find(idVoyage, voyages.values())
                .orElseThrow(() -> new VoyageNotFoundException(idVoyage));

        // 2. Validate Voyage and Clients exist
        LinkedList<Client> reservationClients = new LinkedList<>();

        for (String clientId : clients) {
            Utils.find(clientId, this.clients.values())
                    .map(reservationClients::insertEnd)
                    .orElseThrow(() -> new ClientNotFoundException(clientId));
        }

        // 3. Validate number of clients
        int maxClients = switch (accommodationType) {
            case CABIN2 -> 2;
            case CABIN4 -> 4;
            default -> Integer.MAX_VALUE;
        };
        if (clients.length > maxClients) throw new MaxExceededException();

        Reservation reservation;
        if (idVehicle != null && !idVehicle.isBlank()) {
            reservation = new ParkingReservation(reservationClients, voyage, accommodationType, price, idVehicle);
        } else {
            reservation = new NormalReservation(reservationClients, voyage, accommodationType, price, idVehicle);
        }

        voyage.addReservation(reservation);

        var clientIt = reservationClients.values();
        while (clientIt.hasNext()) {
            Client client = clientIt.next();

            client.addVoyage(voyage);

            Reservation newReservation = reservation.clone();
            LinkedList<Client> newReservationClient = new LinkedList<>();
            newReservationClient.insertEnd(client);
            client.addReservation(newReservation);
        }
    }

    @Override
    public void load(String idClient, String idVoyage, Date dt) throws LoadingAlreadyException, ClientNotFoundException, VoyageNotFoundException, ReservationNotFoundException {
        // 1. Validate client exists
        Client client = Utils.find(idClient, clients.values())
                .orElseThrow(() -> new ClientNotFoundException(idClient));

        // 2. Validate voyage exists
        Voyage voyage = Utils.find(idVoyage, voyages.values())
                .orElseThrow(() -> new VoyageNotFoundException(idVoyage));

        // 3. Reservation exists
        if (!client.containsReservation(voyage)) throw new ReservationNotFoundException(idClient, idVoyage);

        client.loadReservation(voyage);
        voyage.loadReservation(client, dt);
        mostTraveledClients.update(client);
    }

    @Override
    public Iterator<Reservation> unload(String idVoyage) throws VoyageNotFoundException {
        return Utils.find(idVoyage, voyages.values())
                .map(Voyage::unload)
                .orElseThrow(() -> new VoyageNotFoundException(idVoyage));
    }

    @Override
    public int unloadTime(String idVehicle, String idVoyage) throws LandingNotDoneException, VoyageNotFoundException, VehicleNotFoundException {
        Voyage voyage = Utils.find(idVoyage, voyages.values())
                .orElseThrow(() -> new VoyageNotFoundException(idVoyage));

        if (!voyage.isLandingDone()) throw new LandingNotDoneException(idVoyage);

        return voyage.vehicleReservation(idVehicle)
                .map(r -> (ParkingReservation) r)
                .orElseThrow(() -> new VehicleNotFoundException(idVehicle))
                .getUnLoadTimeInMinutes();
    }

    @Override
    public Iterator<Reservation> getClientReservations(String idClient) throws NoReservationException {
        var reservations = Utils.find(idClient, clients.values())
                .map(Client::reservations)
                .orElseThrow(NoReservationException::new);

        if (!reservations.hasNext()) throw new NoReservationException();

        return reservations;
    }

    @Override
    public Iterator<Reservation> getVoyageReservations(String idVoyage) throws NoReservationException {
        var reservations = Utils.find(idVoyage, voyages.values())
                .map(Voyage::reservations)
                .orElseThrow(NoReservationException::new);

        if (!reservations.hasNext()) throw new NoReservationException();

        return reservations;
    }

    @Override
    public Iterator<Reservation> getAccommodationReservations(String idVoyage, AccommodationType accommodationType) throws NoReservationException {
        var reservations = Utils.find(idVoyage, voyages.values())
                .map(v -> v.reservations(accommodationType))
                .orElseThrow(NoReservationException::new);

        if (!reservations.hasNext()) throw new NoReservationException();

        return reservations;
    }

    @Override
    public Client getMostTravelerClient() throws NoClientException {
        if (mostTraveledClients.isEmpty()) throw new NoClientException();
        return mostTraveledClients.values().next();
    }

    @Override
    public Route getMostTraveledRoute() throws NoRouteException {
        if (mostTraveledRoutes.isEmpty()) throw new NoRouteException();
        return mostTraveledRoutes.values().next();
    }

    /***********************************************************************************/
    /******************** AUX OPERATIONS  **********************************************/
    /***********************************************************************************/

    @Override
    public Ship getShip(String id) {
        return Utils.find(id, ships.values()).orElse(null);
    }

    @Override
    public Route getRoute(String idRoute) {
        return Utils.find(idRoute, routes.values()).orElse(null);
    }

    @Override
    public Client getClient(String id) {
        return Utils.find(id, clients.values()).orElse(null);
    }

    @Override
    public Voyage getVoyage(String id) {
        return Utils.find(id, voyages.values()).orElse(null);
    }

    @Override
    public int numShips() {
        return ships.size();
    }

    @Override
    public int numRoutes() {
        return routes.size();
    }

    @Override
    public int numClients() {
        return clients.size();
    }

    @Override
    public int numVoyages() {
        return voyages.size();
    }
}
