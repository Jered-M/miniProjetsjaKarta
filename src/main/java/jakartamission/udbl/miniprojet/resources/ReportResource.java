package jakartamission.udbl.miniprojet.resources;

import java.time.LocalDate;
import java.util.List;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakartamission.udbl.miniprojet.dto.ProductLocationResponse;
import jakartamission.udbl.miniprojet.dto.SaleRecordRequest;
import jakartamission.udbl.miniprojet.dto.SalesAggregateResponse;
import jakartamission.udbl.miniprojet.model.Product;
import jakartamission.udbl.miniprojet.model.SaleRecord;
import jakartamission.udbl.miniprojet.service.InventoryService;
import jakartamission.udbl.miniprojet.service.ReportService;

@Path("reports")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReportResource {

    @Inject
    private ReportService reportService;

    @Inject
    private InventoryService inventoryService;

    @POST
    @Path("/sales")
    public Response recordSale(@Valid SaleRecordRequest request) {
        SaleRecord record = reportService.recordSale(request);
        return Response.ok(record).build();
    }

    @GET
    @Path("/sales")
    public Response getSales(@QueryParam("from") String from,
            @QueryParam("to") String to,
            @QueryParam("groupBy") String groupBy) {
        LocalDate start = from != null && !from.isBlank() ? LocalDate.parse(from) : null;
        LocalDate end = to != null && !to.isBlank() ? LocalDate.parse(to) : null;
        List<SalesAggregateResponse> aggregates = reportService.aggregateSales(start, end, groupBy);
        return Response.ok(aggregates).build();
    }

    @GET
    @Path("/stock-out")
    public Response getOutOfStock() {
        List<Product> products = inventoryService.listOutOfStock();
        List<ProductLocationResponse> response = products.stream()
                .map(p -> new ProductLocationResponse(
                        p.getSku(), p.getName(), p.getLocationCode(), p.getLocationNote(), p.getStockQuantity(),
                        p.getUpdatedAt()))
                .toList();
        return Response.ok(response).build();
    }
}
