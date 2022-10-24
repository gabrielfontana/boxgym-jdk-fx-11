package boxgym.helper;

import boxgym.model.Billing;
import boxgym.model.Customer;
import boxgym.model.Measurement;
import boxgym.model.Membership;
import boxgym.model.Payment;
import boxgym.model.Product;
import boxgym.model.Sale;
import boxgym.model.SaleProduct;
import boxgym.model.Sheet;
import boxgym.model.StockEntry;
import boxgym.model.StockEntryProduct;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class TextFieldFormat {

    public static void currencyFormat(Label label, BigDecimal value) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        label.setText(currencyFormat.format(value));
    }

    public static void currencyFormat(TextField text, BigDecimal value) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        text.setText(currencyFormat.format(value));
    }

    public static void productTableCellCurrencyFormat(TableColumn column) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        column.setCellFactory(tc -> new TableCell<Product, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
    }
    
    public static void stockEntryTableCellCurrencyFormat(TableColumn column) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        column.setCellFactory(tc -> new TableCell<StockEntry, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
    }

    public static void stockEntryProductTableCellCurrencyFormat(TableColumn column) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        column.setCellFactory(tc -> new TableCell<StockEntryProduct, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
    }
    
    public static void saleTableCellCurrencyFormat(TableColumn column) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        column.setCellFactory(tc -> new TableCell<Sale, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
    }
    
    public static void saleProductTableCellCurrencyFormat(TableColumn column) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        column.setCellFactory(tc -> new TableCell<SaleProduct, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
    }

    public static void stockEntryTableCellDateFormat(TableColumn column) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        column.setCellFactory(tc -> new TableCell<StockEntry, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(date.format(dateFormat));
                }
            }
        });
    }

    public static void customerTableCellDateFormat(TableColumn column) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        column.setCellFactory(tc -> new TableCell<Customer, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(date.format(dateFormat));
                }
            }
        });
    }

    public static void saleTableCellDateFormat(TableColumn column) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        column.setCellFactory(tc -> new TableCell<Sale, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(date.format(dateFormat));
                }
            }
        });
    }
    
    public static void measurementTableCellDateFormat(TableColumn column) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        column.setCellFactory(tc -> new TableCell<Measurement, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(date.format(dateFormat));
                }
            }
        });
    }
    
    public static void sheetTableCellDateFormat(TableColumn column) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        column.setCellFactory(tc -> new TableCell<Sheet, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(date.format(dateFormat));
                }
            }
        });
    }
    
    public static void billingTableCellDateFormat(TableColumn column) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        column.setCellFactory(tc -> new TableCell<Billing, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(date.format(dateFormat));
                }
            }
        });
    }
    
    public static void billingTableCellCurrencyFormat(TableColumn column) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        column.setCellFactory(tc -> new TableCell<Billing, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
    }
    
    public static void membershipTableCellDateFormat(TableColumn column) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        column.setCellFactory(tc -> new TableCell<Membership, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(date.format(dateFormat));
                }
            }
        });
    }
    
    public static void membershipTableCellCurrencyFormat(TableColumn column) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        column.setCellFactory(tc -> new TableCell<Membership, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
    }
    
    public static void paymentTableCellDateFormat(TableColumn column) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        column.setCellFactory(tc -> new TableCell<Payment, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(date.format(dateFormat));
                }
            }
        });
    }
    
    public static void paymentTableCellCurrencyFormat(TableColumn column) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        column.setCellFactory(tc -> new TableCell<Payment, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
    }

}
