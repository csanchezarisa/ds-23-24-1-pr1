package uoc.ds.pr;


import uoc.ds.pr.util.DateUtils;

public class FactoryShippingLine {


    public static ShippingLine getShippingLine() throws Exception {
        ShippingLine theShippingLine;
        theShippingLine = new ShippingLineImpl();


        ////
        //// SHIPS
        ////

        theShippingLine.addShip("shipId1", "Queen Mary 2", 500, 100, 200, 150, 12 );
        theShippingLine.addShip("shipId2", "Carnival Vista", 600, 100, 200, 150, 23 );
        theShippingLine.addShip("shipId3", "Celebrity Edge", 600, 100, 200, 150, 32 );
        theShippingLine.addShip("shipId4", "Norwegian Escape", 10, 2, 12, 12, 7 );
        theShippingLine.addShip("shipId5", "Harmony of the Seas", 700, 100, 200, 150, 27 );
        theShippingLine.addShip("shipId6", "MSC Meraviglia", 300, 100, 200, 150, 62 );
        theShippingLine.addShip("shipId7", "Costa Diadema", 130, 100, 200, 150, 52 );

        ////
        //// COMPANIES
        ////
        theShippingLine.addRoute("routeId1", "Barcelona", "Civitavecchia");
        theShippingLine.addRoute("routeId2", "Marsella", "Túnez");
        theShippingLine.addRoute("routeId3", "Nápoles", "Palermo");
        theShippingLine.addRoute("routeId4", "Atenas", "Atenas");
        theShippingLine.addRoute("routeId5", "Barcelona", "Ibiza");
        theShippingLine.addRoute("routeId6", "Málaga", "Tánger");
        theShippingLine.addRoute("routeId7", "Génova", "Estambul");
        theShippingLine.addRoute("routeId8", "Cagliari", "Malta");
        theShippingLine.addRoute("routeId9", "Split", "Dubrovnik");


        ///
        /// Clients
        ///
        theShippingLine.addClient("clientId1", "Maria", "Simó");
        theShippingLine.addClient("clientId2", "Àlex", "Lluna ");
        theShippingLine.addClient("clientId3", "Pepet", "Ferra");
        theShippingLine.addClient("clientId4", "Joana", "Quilez");
        theShippingLine.addClient("clientId5", "Armand", "Morata");
        theShippingLine.addClient("clientId6", "Rut", "Paramio");
        theShippingLine.addClient("clientId7", "Miriam", "Navarro");
        theShippingLine.addClient("clientId8", "Pedro", "Tirrano");
        theShippingLine.addClient("clientId9", "Pedro", "Barón");
        theShippingLine.addClient("clientId10", "Emily", "Jones");
        theShippingLine.addClient("clientId11", "Maria", "Pérez");
        theShippingLine.addClient("clientId12", "Josep", "López");
        theShippingLine.addClient("clientId13", "Rafael", "Vidal");
        theShippingLine.addClient("clientId14", "Joel", "Gràcia");
        theShippingLine.addClient("clientId15", "Josep", "Martí");
        theShippingLine.addClient("clientId16", "Pere", "Jila");
        theShippingLine.addClient("clientId17", "Luis", "Blasco");
        theShippingLine.addClient("clientId18", "Jesus", "Linda");


        ///
        /// VOYAGES
        ///
        theShippingLine.addVoyage("voyageId1", DateUtils.createDate("30-07-2024 22:50:00"),
                DateUtils.createDate("31-07-2024 15:50:00"), "shipId4", "routeId1");

        theShippingLine.addVoyage("voyageId2", DateUtils.createDate("30-07-2024 22:50:00"),
                DateUtils.createDate("31-07-2024 15:50:00"), "shipId2", "routeId2");

        theShippingLine.addVoyage("voyageId3", DateUtils.createDate("30-07-2024 22:50:00"),
                DateUtils.createDate("31-07-2024 15:50:00"), "shipId3", "routeId2");

        return theShippingLine;
    }



}