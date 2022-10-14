package info.hfdb.hfdbapi.Controller.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import info.hfdb.hfdbapi.Controller.Product;
import info.hfdb.hfdbapi.Controller.ProductRowMapper;
import info.hfdb.hfdbapi.Controller.ProductSKU;
import info.hfdb.hfdbapi.Controller.ProductSearchRowMapper;

@Repository
public class HelperDaoImpl implements HelperDao {

    JdbcTemplate jdbcTemplate;

    /**
     * This sets the jdbcTemplate to provide access to the below functions
     *
     * @param jdbcTemplate The jdbcTemplate object being set.
     */
    @Autowired
    public void SearchHelp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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
    public List<ProductSKU> nameSearch(String name, int min, int max) {

        String filter = "";

        if (max != -1)
            filter = "AND price <= " + max + " ";
        if (min != -1)
            filter += "AND price >= " + min;

        String sql = "SELECT products.sku "
                + "FROM products INNER JOIN retailprices ON products.sku=retailprices.sku "
                + "WHERE LOWER(name) LIKE LOWER(?) " + filter + ";";

        ProductSearchRowMapper map = new ProductSearchRowMapper();
        List<ProductSKU> a = jdbcTemplate.query(sql, map, name);
        return a;
    }

    /**
     * this grabProductDetails function returns the product details associated with
     * a given SKU
     *
     * @param sku The sku that data is being returned for
     * @return a product object wrapped in an optional object
     */
    public Optional<Product> grabProductDetails(int sku) {

        int sku1 = sku;
        String sql = """
                SELECT products.sku, name, price, ts, imgurl, canonicalurl
                FROM products INNER JOIN (select retailprices.sku,
                price, ts from retailprices inner join (select sku, max(ts)
                as newestPriceTS from retailprices where sku= ? group by sku)
                as newestPriceCalc on ts = newestPriceTS
                and retailprices.sku = newestPriceCalc.sku) as latestPriceCalc
                ON products.sku=latestPriceCalc.sku AND products.sku = ?
                ORDER BY products;
                            """;
        ProductRowMapper map = new ProductRowMapper();
        List<Product> a = jdbcTemplate.query(sql, map, sku, sku1);
        assert (a.size() <= 1);
        map = null;
        Optional<Product> b = a.stream().findFirst();
        return b;

    }
}
