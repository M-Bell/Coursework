package com.shyiko.coursework.dao;

import com.shyiko.coursework.model.Allergy;
import com.shyiko.coursework.model.Category;
import com.shyiko.coursework.model.UserProfile;
import com.shyiko.coursework.model.UserRating;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Transactional
@Component
public class UtilDao extends BaseDao{

}
