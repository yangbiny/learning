package com.impassive.pay.entity;

import java.util.List;
import lombok.Data;

@Data
public class IapSignHeader {

  private String alg;

  private List<String> x5c;
}