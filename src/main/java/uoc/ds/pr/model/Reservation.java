package uoc.ds.pr.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uoc.ds.pr.ShippingLine;
import uoc.ds.pr.util.DSArray;

import java.util.UUID;

@Data
@EqualsAndHashCode
@ToString
public class Reservation {

    private final String id = UUID.randomUUID().toString();
    private DSArray<String> clients;
    private String idVoyage;
    private ShippingLine.AccommodationType accommodationType;
    private String idVehicle;
    private double price;
}
