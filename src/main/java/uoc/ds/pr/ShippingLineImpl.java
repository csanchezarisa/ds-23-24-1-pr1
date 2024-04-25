package uoc.ds.pr;

import edu.uoc.ds.adt.helpers.Position;
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
    private final OrderedVector<Client> mostTraveledClient = new OrderedVector<>(Comparator.comparingInt(Client::countLoadedReservations));

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

        var position = Utils.findPosition(clients.positions(), client);
        if (position.isEmpty()) {
            clients.insertEnd(client);
        } else {
            clients.update(position.get(), client);
        }
    }

    @Override
    public void addVoyage(String id, Date departureDt, Date arrivalDt, String idShip, String idRoute)
            throws ShipNotFoundException, RouteNotFoundException {

        int shipIdx = Utils.findIndex(idShip, ships.values());
        int routeIdx = Utils.findIndex(idRoute, routes.values());

        if (shipIdx == -1) {
            throw new ShipNotFoundException(idShip);
        }
        if (routeIdx == -1) {
            throw new RouteNotFoundException(idRoute);
        }

        Ship ship = ships.get(shipIdx);
        Route route = routes.get(routeIdx);

        Voyage voyage = new Voyage(id, departureDt, arrivalDt, ship, route);

        var position = Utils.findPosition(voyages.positions(), voyage);
        if (position.isEmpty()) {
            voyages.insertEnd(voyage);
        } else {
            voyages.update(position.get(), voyage);
        }

        ship.addVoyage(voyage);
        ships.modify(shipIdx, ship);

        route.addVoyage(voyage);
        routes.modify(routeIdx, route);
    }

    @Override
    public void reserve(String[] clients, String idVoyage, AccommodationType accommodationType, String idVehicle, double price) throws ClientNotFoundException, VoyageNotFoundException, ParkingFullException, NoAccommodationAvailableException, MaxExceededException, ReservationAlreadyExistsException {

        // 1. Validate voyage exists
        Position<Voyage> voyagePos = Utils.findPosition(idVoyage, voyages.positions())
                .orElseThrow(() -> new VoyageNotFoundException(idVoyage));
        Voyage voyage = voyagePos.getElem();

        // 2. Validate Voyage and Clients exist
        LinkedList<Client> reservationClients = new LinkedList<>();
        Position<Client>[] clientPositions = new Position[clients.length];

        for (int i = 0; i < clients.length; i++) {
            String clientId = clients[i];
            var clientPos = Utils.findPosition(clientId, this.clients.positions())
                    .orElseThrow(() -> new ClientNotFoundException(clientId));

            reservationClients.insertEnd(clientPos.getElem());
            clientPositions[i] = clientPos;
        }

        // 3. Validate number of clients
        int maxClients = switch (accommodationType) {
            case CABIN2 -> 2;
            case CABIN4 -> 4;
            default -> Integer.MAX_VALUE;
        };
        if (clients.length > maxClients) throw new MaxExceededException();

        Reservation reservation = new Reservation(reservationClients, voyage, accommodationType, idVehicle, price);

        voyage.addReservation(reservation);
        voyages.update(voyagePos, voyage);

        for (var clientPos : clientPositions) {
            Client client = clientPos.getElem();
            client.addReservation(reservation);
            client.addVoyage(voyage.getId());
            this.clients.update(clientPos, client);
        }
    }

    @Override
    public void load(String idClient, String idVoyage, Date dt) throws LoadingAlreadyException, ClientNotFoundException, VoyageNotFoundException, ReservationNotFoundException {
        // 1. Validate client exists
        Position<Client> clientPos = Utils.findPosition(idClient, clients.positions())
                .orElseThrow(() -> new ClientNotFoundException(idClient));
        Client client = clientPos.getElem();

        // 2. Validate voyage exists
        Position<Voyage> voyagePos = Utils.findPosition(idVoyage, voyages.positions())
                .orElseThrow(() -> new VoyageNotFoundException(idVoyage));
        Voyage voyage = voyagePos.getElem();

        // 3. Reservation exists
        if (!client.containsReservation(voyage)) throw new ReservationNotFoundException(idClient, idVoyage);

        client.loadReservation(voyage);
        voyage.loadReservation(client, dt);
    }

    @Override
    public Iterator<Reservation> unload(String idVoyage) throws VoyageNotFoundException {
        return null;
    }

    @Override
    public int unloadTime(String idVehicle, String idVoyage) throws LandingNotDoneException, VoyageNotFoundException, VehicleNotFoundException {
        return 0;
    }

    @Override
    public Iterator<Reservation> getClientReservations(String idClient) throws NoReservationException {
        return null;
    }

    @Override
    public Iterator<Reservation> getVoyageReservations(String idVoyage) throws NoReservationException {
        return null;
    }

    @Override
    public Iterator<Reservation> getAccommodationReservations(String idVoyage, AccommodationType accommodationType) throws NoReservationException {
        return null;
    }

    @Override
    public Client getMostTravelerClient() throws NoClientException {
        if (clients.isEmpty()) throw new NoClientException();
        return clients.values().next();
    }

    @Override
    public Route getMostTraveledRoute() throws NoRouteException {
        return null;
    }

    /***********************************************************************************/
    /******************** AUX OPERATIONS  **********************************************/
    /***********************************************************************************/

    @Override
    public Ship getShip(String id) {
        return Utils.find(id, ships.values());
    }

    @Override
    public Route getRoute(String idRoute) {
        return Utils.find(idRoute, routes.values());
    }

    @Override
    public Client getClient(String id) {
        return Utils.find(id, clients.values());
    }

    @Override
    public Voyage getVoyage(String id) {
        return Utils.find(id, voyages.values());
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
