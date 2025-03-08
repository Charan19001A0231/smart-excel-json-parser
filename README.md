# Smart Excel<>Json Parser

## 📌 Project Overview
Smart Excel<>Json Parser is a full-stack application that converts Excel files to JSON and vice versa. It automatically detects the schema from the uploaded Excel file and ensures multiple sheets are encapsulated in a single JSON.

## 🏗 Folder Structure

/your-project
│── backend/         # Spring Boot backend
│── frontend/        # React frontend
│── sample_inputs/     # Sample input files (Excel, JSON)
│── README.md        # Main documentation (this file)
│── .gitignore


## 🏗 Tech Stack
- *Frontend:* React (Vite), Tailwind CSS  
- *Backend:* Spring Boot, Apache POI (for Excel handling)  
- *Others:* Axios, JSON Schema Validation  

## 🚀 Setup & Execution

### 🔧 Prerequisites
- *Java 17+* installed (for Spring Boot)  
- *Node.js 18+* installed (for React)  
- *Maven* installed  

### 📦 Installation Steps

#### *Backend (Spring Boot)*
sh
cd backend
mvn clean install
mvn spring-boot:run

- Backend runs on **http://localhost:8080**  

#### *Frontend (React + Vite)*
sh
cd frontend
npm install
npm run dev

- Frontend runs on **http://localhost:5173**  

## 🛠 API Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/excel-to-json | Converts Excel to JSON |
| POST | /api/json-to-excel | Converts JSON to Excel |

## 📂 Input File Requirements
- Only **`.xlsx`** files are allowed.  
- Users can upload **only one file** at a time.  
 

## 🌟 Features
✅ Convert Excel to JSON  
✅ Convert JSON to Excel  
✅ Supports multiple sheets  
✅ Proper validations for file format  

