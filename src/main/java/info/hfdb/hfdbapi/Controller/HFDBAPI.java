package info.hfdb.hfdbapi.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import info.hfdb.hfdbapi.Controller.Status.StatusCode;
import info.hfdb.hfdbapi.Controller.dao.HelperDao;

@RestController
public class HFDBAPI {
    private final HelperDao helper;

    @Autowired
    public HFDBAPI(@Lazy HelperDao a) {
        super();
        this.helper = a;
    }

    /**
     * This returns the status of the connection and is mapped to '/status'
     *
     * @return status
     */
    @RequestMapping("/status")
    public Status getStatus() {
        return new Status(StatusCode.OK);
    }

    /**
     * This pings and returns a pong and is mapped to '/ping'
     *
     * @return 'pong'
     */
    @RequestMapping("/ping")
    public String ping() {
        return "pong";
    }

    /**
     * this is the nameSearch funtion and is mapped to
     * '/searchName/{name}/{min}/{max}', where the curly braces denote the terms to
     * be searched
     *
     * @param name the name search term
     * @param min  the minimum price search term
     * @param max  the maximum price search term
     * @return a list of ProductSKUs that fit the given parameters
     */
    @GetMapping("/nameSearch/{name}/{min}/{max}")
    public List<ProductSKU> nameSearch(@PathVariable("name") String name, @PathVariable("min") int min,
            @PathVariable("max") int max) {
        List<ProductSKU> a = helper.nameSearch("%" + name + "%", min, max);
        return a;
    }

    /**
     * This is the grabProductDetails function and is mapped to
     * '/grabProductDetails/{sku}', where the curly brace denotes the terms to be
     * searched
     *
     * @param sku the deisred sku to be returned
     * @return a ResponseEntity object that reflects the outcome of the operation
     */
    @GetMapping("/grabProductDetails/{sku}")
    public ResponseEntity<Product> grabDetails(@PathVariable("sku") int sku) {
        Optional<Product> a = helper.grabProductDetails(sku);
        if (a.isEmpty())
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<Product>(a.get(), HttpStatus.OK);
    }
}
