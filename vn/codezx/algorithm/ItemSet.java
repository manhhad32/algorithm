package vn.codezx.algorithm;


import java.util.Set;

public class ItemSet {

 private Set<String> items;
 private double support;

 public ItemSet(Set<String> items, double support) {
  this.items = items;
  this.support = support;
 }

 public Set<String> getItems() {
  return items;
 }

 public double getSupport() {
  return support;
 }

}
