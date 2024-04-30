package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.util.DSArray;
import uoc.ds.pr.util.DSLinkedList;
import uoc.ds.pr.util.OrderedVector;

import java.util.Comparator;
import java.util.Date;


public class ShippingLineImpl implements ShippingLine {

    private final DSArray<Ship> ships = new DSArray<>();
    private final DSArray<Route> routes = new DSArray<>();
    private final DSLinkedList<Client> clients = new DSLinkedList<>();
    private final DSLinkedList<Voyage> voyages = new DSLinkedList<>();
    private final OrderedVector<Client> mostTraveledClients = new OrderedVector<>(1, Comparator.comparingInt(Client::countLoadedReservations));
    private final OrderedVector<Route> mostTraveledRoutes = new OrderedVector<>(1, Comparator.comparingInt(Route::numVoyages)
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

        clients.findPosition(client)
                .ifPresentOrElse(
                        p -> clients.update(p, client),
                        () -> clients.insertEnd(client));
    }

    @Override
    public void addVoyage(String id, Date departureDt, Date arrivalDt, String idShip, String idRoute)
            throws ShipNotFoundException, RouteNotFoundException {

        Ship ship = ships.find(new Ship(idShip))
                .orElseThrow(() -> new ShipNotFoundException(idShip));
        Route route = routes.find(new Route(idRoute))
                .orElseThrow(() -> new RouteNotFoundException(idRoute));

        Voyage voyage = new Voyage(id, departureDt, arrivalDt, ship, route);

        voyages.findPosition(voyage)
                .ifPresentOrElse(
                        p -> voyages.update(p, voyage),
                        () -> voyages.insertEnd(voyage));

        ship.addVoyage(voyage);
        route.addVoyage(voyage);
        mostTraveledRoutes.update(route);
    }

    @Override
    public void reserve(String[] clients, String idVoyage, AccommodationType accommodationType, String idVehicle, double price) throws ClientNotFoundException, VoyageNotFoundException, ParkingFullException, NoAcommodationAvailableException, MaxExceededException, ReservationAlreadyExistsException {

        // 1. Validate voyage exists
        Voyage voyage = voyages.find(new Voyage(idVoyage))
                .orElseThrow(() -> new VoyageNotFoundException(idVoyage));

        // 2. Validate Voyage and Clients exist
        DSLinkedList<Client> reservationClients = new DSLinkedList<>();

        for (String clientId : clients) {
            this.clients.find(new Client(clientId))
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

        // 4. Validate availability
        boolean noAccommodationAvailable = switch (reservation.getAccommodationType()) {
            case ARMCHAIR -> reservation.numClients() > voyage.getAvailableArmChairs();
            case CABIN2 -> voyage.getAvailableCabin2() == 0;
            case CABIN4 -> voyage.getAvailableCabin4() == 0;
        };
        if (noAccommodationAvailable) throw new NoAcommodationAvailableException(reservation.getAccommodationType());

        if (reservation.hasParkingLot() && voyage.getAvailableParkingSlots() == 0) throw new ParkingFullException();

        voyage.addReservation(reservation);

        var clientIt = reservationClients.values();
        while (clientIt.hasNext()) {
            Client client = clientIt.next();

            client.addVoyage(voyage);

            Reservation newReservation = reservation.clone();
            DSLinkedList<Client> newReservationClient = new DSLinkedList<>();
            newReservationClient.insertEnd(client);
            client.addReservation(newReservation);
        }
    }

    @Override
    public void load(String idClient, String idVoyage, Date dt) throws LoadingAlreadyException, ClientNotFoundException, VoyageNotFoundException, ReservationNotFoundException {
        // 1. Validate client exists
        Client client = clients.find(new Client(idClient))
                .orElseThrow(() -> new ClientNotFoundException(idClient));

        // 2. Validate voyage exists
        Voyage voyage = voyages.find(new Voyage(idVoyage))
                .orElseThrow(() -> new VoyageNotFoundException(idVoyage));

        // 3. Reservation exists
        if (!client.containsReservation(voyage)) throw new ReservationNotFoundException(idClient, idVoyage);

        client.loadReservation(voyage);
        voyage.loadReservation(client, dt);
        mostTraveledClients.update(client);
    }

    @Override
    public Iterator<Reservation> unload(String idVoyage) throws VoyageNotFoundException {
        return voyages.find(new Voyage(idVoyage))
                .map(Voyage::unload)
                .orElseThrow(() -> new VoyageNotFoundException(idVoyage));
    }

    @Override
    public int unloadTime(String idVehicle, String idVoyage) throws LandingNotDoneException, VoyageNotFoundException, VehicleNotFoundException {
        Voyage voyage = voyages.find(new Voyage(idVoyage))
                .orElseThrow(() -> new VoyageNotFoundException(idVoyage));

        if (!voyage.isLandingDone()) throw new LandingNotDoneException(idVoyage);

        return voyage.vehicleReservation(idVehicle)
                .map(r -> (ParkingReservation) r)
                .orElseThrow(() -> new VehicleNotFoundException(idVehicle))
                .getUnLoadTimeInMinutes();
    }

    @Override
    public Iterator<Reservation> getClientReservations(String idClient) throws NoReservationException {
        var reservations = clients.find(new Client(idClient))
                .map(Client::reservations)
                .orElseThrow(NoReservationException::new);

        if (!reservations.hasNext()) throw new NoReservationException();

        return reservations;
    }

    @Override
    public Iterator<Reservation> getVoyageReservations(String idVoyage) throws NoReservationException {
        var reservations = voyages.find(new Voyage(idVoyage))
                .map(Voyage::reservations)
                .orElseThrow(NoReservationException::new);

        if (!reservations.hasNext()) throw new NoReservationException();

        return reservations;
    }

    @Override
    public Iterator<Reservation> getAccommodationReservations(String idVoyage, AccommodationType accommodationType) throws NoReservationException {
        var reservations = voyages.find(new Voyage(idVoyage))
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
        return ships.find(new Ship(id)).orElse(null);
    }

    @Override
    public Route getRoute(String idRoute) {
        return routes.find(new Route(idRoute)).orElse(null);
    }

    @Override
    public Client getClient(String id) {
        return clients.find(new Client(id)).orElse(null);
    }

    @Override
    public Voyage getVoyage(String id) {
        return voyages.find(new Voyage(id)).orElse(null);
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
