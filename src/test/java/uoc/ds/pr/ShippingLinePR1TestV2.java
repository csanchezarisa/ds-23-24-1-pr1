package uoc.ds.pr;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.Route;
import uoc.ds.pr.model.Ship;
import uoc.ds.pr.model.Voyage;
import uoc.ds.pr.util.DateUtils;

public class ShippingLinePR1TestV2 {

    private ShippingLine theShippingLine;

    @Before
    public void setUp() throws Exception {
        this.theShippingLine = FactoryShippingLine.getShippingLine();
        Assert.assertEquals(7, this.theShippingLine.numShips());
        Assert.assertEquals(9, this.theShippingLine.numRoutes());
        Assert.assertEquals(18, this.theShippingLine.numClients());
        Assert.assertEquals(3, this.theShippingLine.numVoyages());
    }

    @After
    public void tearDown() {
        this.theShippingLine = null;
    }

    @Test
    public void addVoyageTest() throws DSException {
        // Assert that a voyage is removed when updating ship/route
        theShippingLine.addVoyage("voyageId2000", DateUtils.createDate("30-07-2024 22:50:00"),
                DateUtils.createDate("31-07-2024 15:50:00"), "shipId1", "routeId5");
        Voyage voyage2000 = theShippingLine.getVoyage("voyageId2000");
        Ship firstShip = voyage2000.getShip();
        Route firstRoute = voyage2000.getRoute();
        Assert.assertEquals("shipId1", firstShip.getId());
        Assert.assertEquals(1, firstShip.numVoyages());
        Assert.assertEquals("routeId5", firstRoute.getId());
        Assert.assertEquals(1, firstRoute.numVoyages());

        // Update voyage change ship
        theShippingLine.addVoyage("voyageId2000", DateUtils.createDate("30-07-2024 22:50:00"),
                DateUtils.createDate("31-07-2024 15:50:00"), "shipId2", "routeId5");
        voyage2000 = theShippingLine.getVoyage("voyageId2000");
        Ship secondShip = voyage2000.getShip();
        Assert.assertEquals("shipId2", secondShip.getId());
        Assert.assertEquals(2, secondShip.numVoyages());
        Assert.assertNotEquals(firstShip, secondShip);
        Assert.assertEquals(0, firstShip.numVoyages());
        Assert.assertEquals(firstRoute, voyage2000.getRoute());
        Assert.assertEquals(1, voyage2000.getRoute().numVoyages());

        // Update voyage change route
        theShippingLine.addVoyage("voyageId2000", DateUtils.createDate("30-07-2024 22:50:00"),
                DateUtils.createDate("31-07-2024 15:50:00"), "shipId2", "routeId3");
        voyage2000 = theShippingLine.getVoyage("voyageId2000");
        Route secondRoute = voyage2000.getRoute();
        Assert.assertEquals("shipId2", voyage2000.getShip().getId());
        Assert.assertEquals(secondShip, voyage2000.getShip());
        Assert.assertEquals(2, voyage2000.getShip().numVoyages());
        Assert.assertEquals("routeId3", secondRoute.getId());
        Assert.assertEquals(1, secondRoute.numVoyages());
        Assert.assertNotEquals(firstRoute, secondRoute);
        Assert.assertEquals(0, firstRoute.numVoyages());
    }

    @Test
    public void parkingFullExceptionTest() throws DSException {
        theShippingLine.addShip("shipId1000", "Test", 100, 100, 100, 1, 5);
        theShippingLine.addVoyage("voyageId2000", DateUtils.createDate("30-07-2024 22:50:00"),
                DateUtils.createDate("31-07-2024 15:50:00"), "shipId1000", "routeId1");

        Ship ship = this.theShippingLine.getShip("shipId1000");
        Assert.assertEquals(1, ship.getnParkingSlots());

        Voyage voyage1 = theShippingLine.getVoyage("voyageId2000");
        Assert.assertEquals(1, voyage1.getAvailableParkingSlots());

        String[] clientsA = {"clientId1", "clientId2"};
        theShippingLine.reserve(clientsA, "voyageId2000", ShippingLine.AccommodationType.CABIN2, "2251VV", 200);
        Assert.assertEquals(0, voyage1.getAvailableParkingSlots());

        String[] clientsAx = {"clientId7", "clientId1", "clientId10", "clientId11"};
        Assert.assertThrows(ParkingFullException.class, () ->
                theShippingLine.reserve(clientsAx, "voyageId2000", ShippingLine.AccommodationType.CABIN4, "B1923AT", 200));
    }

    @Test
    public void unloadTimeTest() throws DSException {
        Assert.assertThrows(VoyageNotFoundException.class,
                () -> theShippingLine.unloadTime("vehicleId1", "voyageIdXXXX"));

        // Add reserved
        String[] clientsA = {"clientId1", "clientId2", "clientId3", "clientId4", "clientId5", "clientId6"};
        theShippingLine.reserve(clientsA, "voyageId1", ShippingLine.AccommodationType.ARMCHAIR, "2251VV", 200);

        String[] clientsB = {"clientId7", "clientId8"};
        theShippingLine.reserve(clientsB, "voyageId1", ShippingLine.AccommodationType.CABIN2, "B1923AT", 200);

        String[] clientsC = {"clientId9", "clientId10", "clientId11", "clientId12"};
        theShippingLine.reserve(clientsC, "voyageId1", ShippingLine.AccommodationType.CABIN4, null, 200);

        // Load some reserved
        theShippingLine.load("clientId7", "voyageId1", DateUtils.createDate("30-07-2024 09:30:00"));
        theShippingLine.load("clientId9", "voyageId1", DateUtils.createDate("30-07-2024 09:30:00"));
        theShippingLine.load("clientId10", "voyageId1", DateUtils.createDate("30-07-2024 22:05:00"));
        theShippingLine.load("clientId8", "voyageId1", DateUtils.createDate("30-07-2024 22:05:00"));

        Assert.assertThrows(LandingNotDoneException.class,
                () -> theShippingLine.unloadTime("vehicleId1", "voyageId1"));

        theShippingLine.load("clientId1", "voyageId1", DateUtils.createDate("30-07-2024 22:05:00"));
        theShippingLine.load("clientId2", "voyageId1", DateUtils.createDate("30-07-2024 22:05:00"));

        // Unload
        theShippingLine.unload("voyageId1");

        Assert.assertThrows(VehicleNotFoundException.class,
                () -> theShippingLine.unloadTime("vehicleId1", "voyageId1"));

        Assert.assertEquals(7, theShippingLine.unloadTime("2251VV", "voyageId1"));
        Assert.assertEquals(14, theShippingLine.unloadTime("B1923AT", "voyageId1"));
    }

    @Test
    public void getMostTraveledRouteException() {
        theShippingLine = new ShippingLineImpl();
        Assert.assertThrows(NoRouteException.class, () -> theShippingLine.getMostTraveledRoute());
    }
}