package info.hfdb.hfdbapi.Controller;

import java.util.List;
import java.util.Optional;
import info.hfdb.hfdbapi.HfdbApiApplication;

/**
 * This provides access to the postgres database by wrapping the various
 * operations needed to be performed in corresponding java methods
 */
public class DatabaseWrapper {

    /**
     * this nameSearch function takes the name, a minimum price and a maximum price
     * and returns a list of SKUs fitting those parameters.
     *
     * @param name The name value to serach by (currently is case sensitive)
     * @param min  The minimum price value to search by (can be circumvented by
     *             setting to '-1' in the URL call)
     * @param max  The maximum price value to search by (can be circumvented by
     *             setting to '-1' in the URL call)
     */
    public static List<ProductSKU> nameSearch(String name, int min, int max) {

        createView();
        String filter = "";

        if (max != -1)
            filter = "AND price <= " + max + " ";
        if (min != -1)
            filter += "AND price >= " + min;

        String sql = "SELECT products.sku FROM products, latestprice "
                + "WHERE products.sku = latestprice.sku AND "
                + "LOWER(name) LIKE LOWER(?) " + filter + ";";

        ProductSearchRowMapper map = new ProductSearchRowMapper();
        List<ProductSKU> a = HfdbApiApplication.getJdbcTemplate().query(sql, map, name);
        return a;
    }

    /**
     * this grabProductDetails function returns the product details associated with
     * a given SKU
     *
     * @param sku The sku that data is being returned for
     * @return a product object wrapped in an optional object
     */
    public static Optional<Product> grabProductDetails(int sku) {

        createView();
        String sql = """
                SELECT products.sku, name, price, ts, imgurl, canonicalurl
                FROM products, latestprice WHERE products.sku=latestprice.sku AND products.sku = ?
                ORDER BY products;
                    """;
        ProductRowMapper map = new ProductRowMapper();
        List<Product> a = HfdbApiApplication.getJdbcTemplate().query(sql, map, sku);
        assert (a.size() <= 1);
        map = null;
        Optional<Product> b = a.stream().findFirst();
        return b;

    }

    /**
     * Checks if a view exists, if not it creates one.
     * The view returns a list of skus, prices, and the latest
     * timestamp associated with said skus and prices
     */
    public static void createView() {

        String query = """
                SELECT schemaname, viewname
                FROM pg_catalog.pg_views WHERE schemaname
                NOT IN('pg_catalog', 'information_schema') AND viewname = 'latestprice'
                ORDER BY schemaname, viewname;
                        """;
        CatalogRowMapper map = new CatalogRowMapper();
        List<PgCatalog> a = HfdbApiApplication.getJdbcTemplate().query(query, map);
        int count = a.size();

        if (count == 0) {
            count++;
            String viewCall = """
                    create view latestPrice as
                    select retailprices.sku as sku,price,ts from (
                        retailprices inner join (
                            select sku, max(ts) as maxTS from retailprices group by sku
                        ) as latestPrice on latestprice.sku=retailprices.sku and ts=maxTS
                    );
                    """;
            HfdbApiApplication.getJdbcTemplate().execute(viewCall);
        }
    }
}
