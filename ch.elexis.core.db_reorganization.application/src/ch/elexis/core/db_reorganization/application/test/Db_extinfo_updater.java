package ch.elexis.core.db_reorganization.application.test;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.elexis.data.PersistentObject;

public class Db_extinfo_updater {
	private static ch.rgw.tools.JdbcLink jdbc;
	private static Logger logger = LoggerFactory.getLogger(Db_extinfo_updater.class);
	private static final String FLD_EXTINFO = "ExtInfo";
	private static Connection conn = null;
	private static HashMap<String, Number> fieldsLonger150 = null;

	static void showProgress(String msg){
		System.out.println(msg);
		logger.info(msg);
	}

	/**
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void copy_extinfo(){
		jdbc = PersistentObject.getConnection();
		conn = jdbc.getConnection();
		ArrayList<String> allTables = jdbcGetAllTableNames();
		for (int j = 0; j < allTables.size(); j++) {
			jdbcCopyTableAndConvertExtInfo(allTables.get(j));
		}
	}

	public static ArrayList<String> jdbcGetAllTableNames(){
		ArrayList<String> res = new ArrayList<String>();
		try {
			DatabaseMetaData dmd = conn.getMetaData();
			String[] onlyTables = {
				"TABLE"
			};
			ResultSet rs = dmd.getTables(null, null, "%", onlyTables);
			if (rs != null) {
				while (rs.next()) {
					// DatabaseMetaData#getTables() specifies TABLE_NAME is in
					// column 3
					res.add(rs.getString(3));
				}
			}
		} catch (SQLException je) {
			showProgress("SQLException beim Abrufen der Datenbank Tabellen Information. "
				+ je.getMessage());
		}
		return res;
	}

	/*
	 * return all fields of table (in lowercase)
	 */
	public static ArrayList<String> jdbcGetAllFields(String table){
		ArrayList<String> fields = new ArrayList<String>();
		Statement stmt = null;
		String query = "Select * from " + table;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			ResultSetMetaData md = rs.getMetaData();
			int col = md.getColumnCount();
			for (int i = 1; i <= col; i++) {
				fields.add(md.getColumnName(i).toLowerCase());
			}
		} catch (SQLException e1) {
			showProgress("Fehler beim Abrufen der Felder der Tabelle" + e1.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					showProgress("SQLException bei stmt.close");
				}
			}
		}
		return fields;
	}

	// From PersistenceObject
	@SuppressWarnings("unchecked")
	private static Hashtable<Object, Object> fold(final byte[] flat){
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(flat);
			ZipInputStream zis = new ZipInputStream(bais);
			zis.getNextEntry();
			ObjectInputStream ois = new ObjectInputStream(zis);
			Hashtable<Object, Object> res = (Hashtable<Object, Object>) ois.readObject();
			ois.close();
			bais.close();
			return res;
		} catch (Exception ex) {
			// ExHandler.handle(ex);
			return null;
		}
	}

	// Adapted and modified from PersistenceOjbect
	@SuppressWarnings("rawtypes")
	private static Map mimicGetMap(ResultSet rs, final String field){
		byte[] blob;
		try {
			blob = rs.getBytes(field);
		} catch (SQLException e) {
			showProgress("mimicGetMap SQLException " + e.getMessage());
			return new Hashtable();
		}
		if (blob == null) {
			return new Hashtable();
		}
		Hashtable<Object, Object> ret = fold(blob);
		if (ret == null) {
			return new Hashtable();
		}
		return ret;
	}

	/*
	 * Return all fields mentioned in extinfo (lowercase) if not already present in
	 * other fields.
	 */
	public static ArrayList<String> jdbcGetExtInfoForTable(String table)

	{
		fieldsLonger150 = new HashMap<String, Number>();
		ArrayList<String> fields = jdbcGetAllFields(table);
		ArrayList<String> extFields = new ArrayList<String>();
		Statement stmt = null;
		ResultSet rs = null;
		int maxLength = 0;
		int j = 0;

		// Get all extinfo which are not null
		String query = "Select * from " + table + " where extInfo is not null";
		showProgress("Starting Query " + query);
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				j++;
				String extInfo = rs.getString(FLD_EXTINFO);
				Map<String, String> maps = mimicGetMap(rs, FLD_EXTINFO);
				Iterator<?> x = maps.entrySet().iterator();
				while (x.hasNext()) {
					Object obj = x.next();
					Entry ht = (Entry) obj;
					String fieldName = ht.getKey().toString().toLowerCase();
					String validFieldName = getValidFieldName(fieldName);
					int length = ht.getValue().toString().length();
					if (!extFields.contains(fieldName) && !fields.contains(fieldName))
						extFields.add(fieldName);
					// showProgress(String.format("found <%1$s -> %2$s>", fieldName,
					// ht.getValue().toString()));
					if (maxLength < length)
						maxLength = length;
					if (length > 150) {
						if (fieldsLonger150.get(validFieldName) == null)
							fieldsLonger150.put(validFieldName, length);
						else if (fieldsLonger150.get(validFieldName).intValue() < length)
							fieldsLonger150.put(validFieldName, length);
					}
				}
			}
			stmt.close();

		} catch (SQLException e1) {
			showProgress("SQLException beim Abrufen extinfo der Tabelle " + table + " "
				+ e1.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					showProgress("SQLException bei stmt.close");
				}
			}
		}
		showProgress(String.format("Query %3$s returned %1$d rows\n%2$s longs %3$s", j,
			extFields.toString(), query, fieldsLonger150.toString()));
		return extFields;
	}

	public static String getValidFieldName(String oldFieldName){
		// statusänderung or zurückgewiesen are found as column names via extinfo
		String newFieldName = oldFieldName.replaceAll("[^a-zA-Zäöü_]", "_").toLowerCase();
		// https://dev.mysql.com/doc/refman/5.5/en/reserved-words.html contains fulltext
		ArrayList<String> reservedWords =
			new ArrayList<String>(Arrays.asList("fulltext", "int", "word"));
		if (reservedWords.contains(newFieldName))
			newFieldName = newFieldName + "_add";
		return newFieldName;
	}

	public static void jdbcConvertExtInfo(String table){
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		int j = 0;
		String fieldName = "";
		String value = "";

		// - Extract all info from the extInfo
		// - Update the row with the new value and set extInfo to null

		query = "Select * from " + table + " where extInfo is not null";
		showProgress("Starting Query " + query);
		try {
			stmt =
				conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			conn.setAutoCommit(false);

			rs = stmt.executeQuery(query);
			while (rs.next()) {
				j++;
				if (j % 8000 == 1) {
					System.out.print(String.format("\n%1$20s: %2$6d ", table, j - 1));
					conn.commit();
				}
				if (j % 100 == 0)
					System.out.print(".");
				Map<String, String> maps = mimicGetMap(rs, "extInfo");
				Iterator<?> x = maps.entrySet().iterator();
				while (x.hasNext()) {
					Object obj = x.next();
					Entry<?, ?> ht = (Entry<?, ?>) obj;
					fieldName = getValidFieldName(ht.getKey().toString());
					value = ht.getValue().toString();
					rs.updateString(fieldName, value);
				}
				rs.updateString(FLD_EXTINFO, null);
				rs.updateRow();
			}
			stmt.close();

		} catch (SQLException e1) {
			showProgress("SQLException beim Updaten der extinfo der Tabelle " + table + " "
				+ e1.getMessage());
			String msg =
				String.format("%1$s %2$d field %3$s value %4$s", table, j, fieldName, value);
			showProgress(msg);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					showProgress("Fehler bei stmt.close" + table + " " + e.getMessage());
				}
			}
		}
		showProgress(String.format("Table %1$s updated %2$d rows", table, j));

	}

	public static int getFieldLength(String fieldName){
		if (fieldsLonger150.get(fieldName) != null)
			// Add some margin
			return fieldsLonger150.get(fieldName).intValue() * 2;
		else
			return 255;
	}

	public static void jdbcCopyTableAndConvertExtInfo(String table){
		// Return when running on a copy
		if (table.toLowerCase().endsWith("_copy")) {
			showProgress("Skipping as" + table + " ends with _copy");
			return;
		}

		// Return if we don't have a field extinfo
		ArrayList<String> fields = jdbcGetAllFields(table);
		boolean found = false;
		int j = 0;
		for (j = 0; j < fields.size(); j++)
			if (fields.get(j).compareToIgnoreCase(FLD_EXTINFO) == 0) {
				found = true;
			}
		if (!found) {
			showProgress("Skipping as no field extinfo in " + table);
			return;
		}

		String query = null;
		Statement stmt = null;
		ArrayList<String> copyStmts = new ArrayList<String>();
		copyStmts.add(String.format("DROP TABLE IF EXISTS %1$s_COPY", table));
		copyStmts.add(String.format("CREATE TABLE %1$s_COPY LIKE %1$s", table));
		copyStmts.add(String.format("INSERT %1$s_COPY SELECT * FROM %1$s", table));

		String tableCopy = String.format("%1$s_COPY", table);
		try {
			for (int k = 0; k < copyStmts.size(); k++) {
				query = copyStmts.get(k);
				showProgress(query);
				stmt = conn.createStatement();
				stmt.executeUpdate(query);
				stmt.close();
			}
		} catch (SQLException e1) {
			showProgress("SQLException beim Abrufen von " + query + " " + e1.getLocalizedMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					showProgress("SQLException bei stmt.close" + query);
				}
			}
		}

		table = tableCopy;
		Method method = null;
		try {
			StringBuilder sb = null;
			String extFieldName;
			// Now we will get the info about alle extinfo fields
			ArrayList<String> extFields = jdbcGetExtInfoForTable(table);
			// Now add all the extInfo fields as database fields
			// But watch out for invalid names!
			showProgress("fields are: " + fields);
			if (extFields.size() > 0) {
				showProgress(table + " extFields to add are: " + extFields);
				for (j = 1; j < extFields.size(); j++) {
					extFields.set(j, getValidFieldName(extFields.get(j)));
				}
				sb = null;
				for (j = 0; j < extFields.size(); j++) {
					extFieldName = extFields.get(j);
					showProgress("Table "+table +": Adding extFieldName: " + extFieldName);
					if (sb == null) {
						sb =
							new StringBuilder(String.format(
								"Alter table %1$s add %2$s VARCHAR(%3$d)", table, extFieldName,
								getFieldLength(extFieldName)));
					} else {
						sb.append(String.format(", add %1$s VARCHAR(%2$s)", extFieldName,
							getFieldLength(extFieldName)));
					}
				}
				String addStatement = sb.toString();
				try {
					showProgress(addStatement);
					Statement addStmt;
					addStmt = conn.createStatement();
					addStmt.executeUpdate(addStatement);
					addStmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					showProgress("SQLException: " + addStatement + "\n" + e.getMessage());
				}
			}

			// Now we have all the info and will iterate over the copied table
			jdbcConvertExtInfo(table);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			showProgress("SecurityException executing " + method.getName());
		}

		showProgress(String.format("Query %3$s returned %1$d rows\n%2$s", j, fields.toString(),
			query));
	}

}