package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Food> getFoodsByPortions(int portions) {
		
		String sql = "select f.food_code as fcode, f.display_name as fname, "
				+ "count(distinct p.portion_id) as porzioni " + 
				"from food as f, portion as p " + 
				"where f.food_code = p.food_code " + 
				"group by f.food_code " + 
				"having porzioni >= ? " +
				"order by f.display_name ASC";
		
		List<Food> result = new ArrayList<>(); 
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, portions);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				result.add(new Food(rs.getInt("fcode"), rs.getString("fname")));
			}
			
			conn.close();
			return result;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Double calorieCongiunte(Food f1, Food f2) {
		
		String sql = "SELECT fc1.food_code as fcode1, fc2.food_code as fcode2, " + 
				"avg(c.condiment_calories) as mediaCalorie " + 
				"FROM food_condiment as fc1, food_condiment as fc2, condiment as c " + 
				"WHERE fc1.condiment_code = fc2.condiment_code " + 
				"AND fc1.condiment_code = c.condiment_code " + 
				"AND fc1.id <> fc2.id " + 
				"AND fc1.food_code = ? " + 
				"AND fc2.food_code = ? " + 
				"GROUP BY fc1.food_code, fc2.food_code";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, f1.getFood_code());
			st.setInt(2, f2.getFood_code());
			ResultSet rs = st.executeQuery();
			
			Double calories = null;
			if(rs.first()) {
				calories = rs.getDouble("mediaCalorie");
			} // altrimenti rimane null
			
			conn.close();
			return calories;
						
			
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
