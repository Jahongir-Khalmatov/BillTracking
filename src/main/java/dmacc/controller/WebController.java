/**
 * @author Bryner Gibson - bpgibson1
 * CIS175 - Fall 2021
 * Nov 16, 2021
 */
package dmacc.controller;

import dmacc.beans.Employee;
import dmacc.repository.EmployeeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import dmacc.beans.Bill;
import dmacc.repository.BillRepository;

import java.util.Optional;

@Controller
public class WebController {

    @Autowired
    BillRepository repo;
    @Autowired
    EmployeeRepository employeeRepository;

    // should be used for testing, only the manager should see all results
    @GetMapping("viewAll")
    public String viewAllBills(Model model, Employee employee) {
        if (employee.getManagerFlag() == 1)

            if (repo.findAll().isEmpty()) {
                return loginScreen(model);
            }


        model.addAttribute("bills", repo.findAll());
        return "Results";
    }

    @GetMapping({"/", "loginScreen"})
    public String loginScreen(Model model) {

        //TODO: this will only display login screen with manager, employee, and customer links

        return "login";
    }

    @GetMapping("/inputBill")
    public String addNewBill(Model model) {

        Bill b = new Bill();
        model.addAttribute("newBill", b);
        return "input";
    }

    // Done  This returns all bills of employee
    @GetMapping("/viewBillByEmployee/{id}")
    public String viewBillByEmployee(@PathVariable long id, Model model) {

        model.addAttribute("bills", repo.findAllByEmployee_Id(id));
        return "Results";
    }

    // Done This returns All payed bills of employee
    @GetMapping("/viewAllPayedBills")
    public String viewAllPayedBills(@PathVariable long id, Model model) {
        model.addAttribute("payedBills", repo.findAllByEmployee_IdAndIsPayedTrue(id));
        return "PayedBills";
    }

    @PutMapping("/editBills/{id}")
    public String editBillsById(@PathVariable long id, @PathVariable long flagManager, @RequestBody Bill bill, Model model) {
        if (flagManager == 1) {
            Optional<Bill> optionalBill = repo.findById(id);
            if (optionalBill.isPresent()) {
                Bill bill1 = new Bill();
                bill1.setDescription(bill.getDescription());
                Optional<Employee> optionalEmployee = employeeRepository.findById(bill.getEmployee().getId());
                if (!optionalEmployee.isPresent()) {
                    return "Employee Id not found";
                }
                Employee employee = optionalEmployee.get();
bill1.setEmployee(employee);
bill1.setActivity(bill.getActivity());
bill1.setDescription(bill.getDescription());
bill1.setIsPayed(bill.getIsPayed());
bill1.setTax(bill.getTax() );
bill1.setTotal(bill.getTotal());
repo.save(bill1);
model.addAttribute("success", repo.findById(id));
return "success";
            }
            return "Bill id not founded";
        }
        return "You do not have permission";
    }
    @PostMapping("/addBill")
    public String addBills(Model model , @RequestBody Bill bill,@PathVariable Long flagManager){
        if (flagManager == 1) {
                Bill bill1 = new Bill();
                bill1.setDescription(bill.getDescription());
                Optional<Employee> optionalEmployee = employeeRepository.findById(bill.getEmployee().getId());
                if (!optionalEmployee.isPresent()) {
                    return "Employee Id not found";
                }
                Employee employee = optionalEmployee.get();
                bill1.setEmployee(employee);
                bill1.setActivity(bill.getActivity());
                bill1.setDescription(bill.getDescription());
                bill1.setIsPayed(bill.getIsPayed());
                bill1.setTax(bill.getTax());
                bill1.setTotal(bill.getTotal());
                repo.save(bill1);
            return "success";
            }
            return "Bill id not founded";


    }
    //
    @DeleteMapping("/delete/{id}")
    public String deleteBillById(@PathVariable Long id,@PathVariable Long managerFlag,Model model){
        if (managerFlag!=1){
            return "you do not have permission to delete";
        }
        Optional<Bill> optionalBill = repo.findById(id);
if (optionalBill.isEmpty()){
    return "bill not founded";
}
        Bill bill = optionalBill.get();
    repo.delete(bill);
    return "deleted";


    }
@GetMapping("/getBillById/{id}")
    public Bill getBillById(@PathVariable Long id,Model model,Long managerFlag){
    Optional<Bill> optionalBill = repo.findById(id);
    if (managerFlag!=0){
    return null;
    }
    Bill bill = optionalBill.get();
    return bill;
}

}
