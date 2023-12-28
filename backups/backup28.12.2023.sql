--
-- PostgreSQL database dump
--

-- Dumped from database version 16.1
-- Dumped by pg_dump version 16.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: bds; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA bds;


ALTER SCHEMA bds OWNER TO postgres;

--
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA bds;


--
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


--
-- Name: booking_status_select; Type: TYPE; Schema: bds; Owner: postgres
--

CREATE TYPE bds.booking_status_select AS ENUM (
    'pending',
    'confirmed',
    'cancelled'
);


ALTER TYPE bds.booking_status_select OWNER TO postgres;

--
-- Name: gender_select; Type: TYPE; Schema: bds; Owner: postgres
--

CREATE TYPE bds.gender_select AS ENUM (
    'none',
    'male',
    'female',
    'other'
);


ALTER TYPE bds.gender_select OWNER TO postgres;

--
-- Name: payment_method_select; Type: TYPE; Schema: bds; Owner: postgres
--

CREATE TYPE bds.payment_method_select AS ENUM (
    'payment card',
    'cash',
    'bank transfer',
    'paypal',
    'bitcoin'
);


ALTER TYPE bds.payment_method_select OWNER TO postgres;

--
-- Name: payment_status_select; Type: TYPE; Schema: bds; Owner: postgres
--

CREATE TYPE bds.payment_status_select AS ENUM (
    'successful',
    'pending',
    'failed',
    'refunded'
);


ALTER TYPE bds.payment_status_select OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: address; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.address (
    address_id integer NOT NULL,
    country_id integer NOT NULL,
    city_id integer NOT NULL,
    street character varying(50) NOT NULL,
    building_number character varying(20) NOT NULL,
    postal_code character varying(20) NOT NULL
);


ALTER TABLE bds.address OWNER TO postgres;

--
-- Name: address_address_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.address_address_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE bds.address_address_id_seq OWNER TO postgres;

--
-- Name: address_address_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.address_address_id_seq OWNED BY bds.address.address_id;


--
-- Name: aircraft; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.aircraft (
    aircraft_id integer NOT NULL,
    aircraft_model_id integer NOT NULL,
    registration_code character varying(8) NOT NULL,
    manufacture_year integer NOT NULL,
    capacity integer
);


ALTER TABLE bds.aircraft OWNER TO postgres;

--
-- Name: aircraft_aircraft_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.aircraft_aircraft_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE bds.aircraft_aircraft_id_seq OWNER TO postgres;

--
-- Name: aircraft_aircraft_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.aircraft_aircraft_id_seq OWNED BY bds.aircraft.aircraft_id;


--
-- Name: aircraft_model; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.aircraft_model (
    aircraft_model_id integer NOT NULL,
    model character varying(50) NOT NULL,
    fare_per_unit numeric(3,2) NOT NULL
);


ALTER TABLE bds.aircraft_model OWNER TO postgres;

--
-- Name: aircraft_model_aircraft_model_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.aircraft_model_aircraft_model_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE bds.aircraft_model_aircraft_model_id_seq OWNER TO postgres;

--
-- Name: aircraft_model_aircraft_model_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.aircraft_model_aircraft_model_id_seq OWNED BY bds.aircraft_model.aircraft_model_id;


--
-- Name: aircraft_model_travel_class_pricing; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.aircraft_model_travel_class_pricing (
    aircraft_model_id integer NOT NULL,
    travel_class_id integer NOT NULL,
    travel_class_fare_multiplier numeric(3,2) NOT NULL
);


ALTER TABLE bds.aircraft_model_travel_class_pricing OWNER TO postgres;

--
-- Name: airport; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.airport (
    airport_id integer NOT NULL,
    name character varying(75) NOT NULL,
    iata_code character varying(3) NOT NULL,
    icao_code character varying(4) NOT NULL,
    latitude numeric(8,6) NOT NULL,
    longitude numeric(9,6) NOT NULL,
    country_id integer
);


ALTER TABLE bds.airport OWNER TO postgres;

--
-- Name: airport_airport_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.airport_airport_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE bds.airport_airport_id_seq OWNER TO postgres;

--
-- Name: airport_airport_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.airport_airport_id_seq OWNED BY bds.airport.airport_id;


--
-- Name: booking; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.booking (
    booking_id integer NOT NULL,
    booking_status bds.booking_status_select DEFAULT 'pending'::bds.booking_status_select,
    booking_made timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    flight_id integer NOT NULL,
    person_id integer NOT NULL,
    seat_id integer NOT NULL
);


ALTER TABLE bds.booking OWNER TO postgres;

--
-- Name: booking_booking_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.booking_booking_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE bds.booking_booking_id_seq OWNER TO postgres;

--
-- Name: booking_booking_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.booking_booking_id_seq OWNED BY bds.booking.booking_id;


--
-- Name: booking_payment; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.booking_payment (
    booking_payment_id integer NOT NULL,
    booking_id integer NOT NULL,
    amount numeric(5,2) NOT NULL,
    payment_method bds.payment_method_select NOT NULL,
    payment_status bds.payment_status_select NOT NULL
);


ALTER TABLE bds.booking_payment OWNER TO postgres;

--
-- Name: booking_payment_booking_payment_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.booking_payment_booking_payment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE bds.booking_payment_booking_payment_id_seq OWNER TO postgres;

--
-- Name: booking_payment_booking_payment_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.booking_payment_booking_payment_id_seq OWNED BY bds.booking_payment.booking_payment_id;


--
-- Name: city; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.city (
    city_id integer NOT NULL,
    city character varying(50) NOT NULL
);


ALTER TABLE bds.city OWNER TO postgres;

--
-- Name: city_city_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.city_city_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE bds.city_city_id_seq OWNER TO postgres;

--
-- Name: city_city_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.city_city_id_seq OWNED BY bds.city.city_id;


--
-- Name: country; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.country (
    country_id integer NOT NULL,
    country character varying(50) NOT NULL
);


ALTER TABLE bds.country OWNER TO postgres;

--
-- Name: country_country_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.country_country_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE bds.country_country_id_seq OWNER TO postgres;

--
-- Name: country_country_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.country_country_id_seq OWNED BY bds.country.country_id;


--
-- Name: flight_schedule; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.flight_schedule (
    flight_id integer NOT NULL,
    origin_airport_id integer NOT NULL,
    destination_airport_id integer NOT NULL,
    aircraft_id integer NOT NULL,
    departure_dt timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    arrival_dt timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_scheduled boolean NOT NULL
);


ALTER TABLE bds.flight_schedule OWNER TO postgres;

--
-- Name: flight_schedule_flight_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.flight_schedule_flight_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE bds.flight_schedule_flight_id_seq OWNER TO postgres;

--
-- Name: flight_schedule_flight_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.flight_schedule_flight_id_seq OWNED BY bds.flight_schedule.flight_id;


--
-- Name: person; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.person (
    person_id integer NOT NULL,
    first_name character varying(35),
    family_name character varying(35),
    date_of_birth date,
    gender bds.gender_select,
    email character varying(255) NOT NULL,
    phone character varying(20),
    address_id integer,
    balance integer,
    password_hash character varying(255) NOT NULL
);


ALTER TABLE bds.person OWNER TO postgres;

--
-- Name: person_has_role; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.person_has_role (
    person_id integer NOT NULL,
    role_id integer NOT NULL
);


ALTER TABLE bds.person_has_role OWNER TO postgres;

--
-- Name: person_person_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.person_person_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE bds.person_person_id_seq OWNER TO postgres;

--
-- Name: person_person_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.person_person_id_seq OWNED BY bds.person.person_id;


--
-- Name: role; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.role (
    role_id integer NOT NULL,
    role character varying(30) NOT NULL
);


ALTER TABLE bds.role OWNER TO postgres;

--
-- Name: role_role_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.role_role_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE bds.role_role_id_seq OWNER TO postgres;

--
-- Name: role_role_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.role_role_id_seq OWNED BY bds.role.role_id;


--
-- Name: seat; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.seat (
    seat_id integer NOT NULL,
    seat_number integer NOT NULL,
    aircraft_model_id integer NOT NULL,
    travel_class_id integer NOT NULL
);


ALTER TABLE bds.seat OWNER TO postgres;

--
-- Name: seat_seat_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.seat_seat_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE bds.seat_seat_id_seq OWNER TO postgres;

--
-- Name: seat_seat_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.seat_seat_id_seq OWNED BY bds.seat.seat_id;


--
-- Name: sqlinjection; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.sqlinjection (
    person_id integer NOT NULL,
    first_name character varying(20),
    last_name character varying(30)
);


ALTER TABLE bds.sqlinjection OWNER TO postgres;

--
-- Name: sqlinjection_person_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.sqlinjection_person_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE bds.sqlinjection_person_id_seq OWNER TO postgres;

--
-- Name: sqlinjection_person_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.sqlinjection_person_id_seq OWNED BY bds.sqlinjection.person_id;


--
-- Name: travel_class; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.travel_class (
    travel_class_id integer NOT NULL,
    class character varying(12) NOT NULL
);


ALTER TABLE bds.travel_class OWNER TO postgres;

--
-- Name: travel_class_travel_class_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.travel_class_travel_class_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE bds.travel_class_travel_class_id_seq OWNER TO postgres;

--
-- Name: travel_class_travel_class_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.travel_class_travel_class_id_seq OWNED BY bds.travel_class.travel_class_id;


--
-- Name: address address_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.address ALTER COLUMN address_id SET DEFAULT nextval('bds.address_address_id_seq'::regclass);


--
-- Name: aircraft aircraft_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.aircraft ALTER COLUMN aircraft_id SET DEFAULT nextval('bds.aircraft_aircraft_id_seq'::regclass);


--
-- Name: aircraft_model aircraft_model_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.aircraft_model ALTER COLUMN aircraft_model_id SET DEFAULT nextval('bds.aircraft_model_aircraft_model_id_seq'::regclass);


--
-- Name: airport airport_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.airport ALTER COLUMN airport_id SET DEFAULT nextval('bds.airport_airport_id_seq'::regclass);


--
-- Name: booking booking_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.booking ALTER COLUMN booking_id SET DEFAULT nextval('bds.booking_booking_id_seq'::regclass);


--
-- Name: booking_payment booking_payment_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.booking_payment ALTER COLUMN booking_payment_id SET DEFAULT nextval('bds.booking_payment_booking_payment_id_seq'::regclass);


--
-- Name: city city_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.city ALTER COLUMN city_id SET DEFAULT nextval('bds.city_city_id_seq'::regclass);


--
-- Name: country country_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.country ALTER COLUMN country_id SET DEFAULT nextval('bds.country_country_id_seq'::regclass);


--
-- Name: flight_schedule flight_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.flight_schedule ALTER COLUMN flight_id SET DEFAULT nextval('bds.flight_schedule_flight_id_seq'::regclass);


--
-- Name: person person_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.person ALTER COLUMN person_id SET DEFAULT nextval('bds.person_person_id_seq'::regclass);


--
-- Name: role role_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.role ALTER COLUMN role_id SET DEFAULT nextval('bds.role_role_id_seq'::regclass);


--
-- Name: seat seat_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.seat ALTER COLUMN seat_id SET DEFAULT nextval('bds.seat_seat_id_seq'::regclass);


--
-- Name: sqlinjection person_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.sqlinjection ALTER COLUMN person_id SET DEFAULT nextval('bds.sqlinjection_person_id_seq'::regclass);


--
-- Name: travel_class travel_class_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.travel_class ALTER COLUMN travel_class_id SET DEFAULT nextval('bds.travel_class_travel_class_id_seq'::regclass);


--
-- Data for Name: address; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.address (address_id, country_id, city_id, street, building_number, postal_code) FROM stdin;
1	1	1	Maple St	321	80331
2	2	2	Cedar St	654	08003
3	3	3	Oak St	789	04578
4	4	4	Elm St	456	560001
5	5	5	Birch St	876	200002
6	6	6	Pine St	654	01000
7	7	7	Walnut St	987	1012AB
8	8	8	Oak St	321	04556
9	9	9	Birch St	123	11432
10	10	10	Cedar St	456	20100
11	11	11	Elm St	789	H3B1J5
12	12	12	Pine St	654	123456
13	13	13	Maple St	987	100-0001
14	14	14	Oak St	321	101000
15	15	15	Cedar St	456	C1002AAA
16	16	16	Elm St	789	10001
17	17	17	Maple St	123	SW1A1AA
18	18	18	Pine St	654	75001
19	19	19	Elm St	987	2000
20	20	20	Birch St	321	70173
21	2	21	Cedar St	456	41001
22	20	22	River Lane	1	D01X9R5
23	21	23	Acropolis St	42	10558
24	22	24	Ocean View Dr	8	1100-007
25	23	25	Bosphorus St	7	34457
26	24	26	Fjord St	65	0257
27	25	27	Castle Road	13	00-001
28	26	28	Swiss Alps Dr	26	8001
29	27	29	Danube St	29	1010
30	28	30	Chocolate Ave	17	1000
31	29	31	Castle Hill	5	11000
32	9	9	Viking St	88	10321
33	30	32	Northern Lights Rd	21	00100
34	31	33	Harbor View Dr	37	1057
35	32	34	Parliament St	61	1054
36	33	35	Carpathian St	74	010191
37	34	36	Balkan St	99	1000
38	35	37	Adriatic St	10	10000
39	36	38	Castle Square	8	1000
40	37	39	Danube Dr	54	11000
41	38	40	Miljacka St	23	71000
42	39	41	Black Lake Rd	14	81000
43	40	42	Bunker St	6	1001
44	41	43	Vardar St	32	1000
45	42	44	Independence St	45	10000
46	43	45	Grand Duke St	11	1234
47	44	46	Baltic Sea St	18	10145
\.


--
-- Data for Name: aircraft; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.aircraft (aircraft_id, aircraft_model_id, registration_code, manufacture_year, capacity) FROM stdin;
1	4	COF183	2016	220
2	3	XYZ789	2017	220
3	1	LMN456	2018	220
4	2	DEF123	2015	220
5	1	PQR789	2019	220
6	1	GHI123	2014	220
7	1	JKL456	2016	220
8	8	VWX789	2017	220
9	2	NOP789	2015	220
10	1	STU456	2019	220
11	2	QRS123	2016	220
12	2	BCD456	2017	220
13	1	EFG789	2014	220
14	2	HIJ123	2018	220
15	2	KLM456	2019	220
16	1	ZAB123	2017	220
17	2	BA987	2015	220
18	2	AB432	2017	220
19	2	EM901	2016	220
20	2	BD654	2015	220
21	3	BB129	2018	220
22	3	AA789	2015	220
23	3	EE234	2017	220
24	1	BQ312	2014	220
25	3	AA456	2014	220
26	4	BB789	2019	220
27	6	EE456	2016	220
28	3	BC123	2017	220
29	8	AB555	2019	220
30	3	BD111	2017	220
31	1	BQ678	2016	220
32	4	AA777	2015	220
33	7	EE789	2015	220
34	4	BQ555	2017	220
35	5	BB999	2016	220
36	3	AB777	2017	220
37	5	EE111	2015	220
38	5	BC678	2014	220
39	4	BD555	2018	220
40	5	AB666	2019	220
41	5	EE786	2016	220
42	7	BQ444	2015	220
43	6	AB999	2019	220
44	3	BD666	2017	220
45	4	EE123	2015	220
46	7	BC555	2018	220
47	6	CZ589	1999	220
48	7	FR124	1989	220
49	1	KK976	2005	220
50	3	CZ149	1999	220
\.


--
-- Data for Name: aircraft_model; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.aircraft_model (aircraft_model_id, model, fare_per_unit) FROM stdin;
1	Airbus A220	3.45
2	Airbus A330neo	4.45
3	Airbus A321	3.80
4	Airbus A380	2.35
5	Boeing 747	4.75
6	Boeing 737 MAX	3.30
7	Embraer E140	5.05
8	Embraer E190	2.10
\.


--
-- Data for Name: aircraft_model_travel_class_pricing; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.aircraft_model_travel_class_pricing (aircraft_model_id, travel_class_id, travel_class_fare_multiplier) FROM stdin;
1	1	1.75
1	3	3.80
1	4	5.10
2	1	1.90
2	3	4.00
2	4	6.20
3	1	1.50
3	2	2.40
3	3	4.99
3	4	9.50
4	1	1.25
4	2	1.90
5	1	1.10
5	3	3.20
5	4	7.50
6	1	1.35
7	1	1.20
7	2	1.75
7	3	5.50
7	4	9.50
8	1	2.25
8	2	2.50
8	4	7.00
\.


--
-- Data for Name: airport; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.airport (airport_id, name, iata_code, icao_code, latitude, longitude, country_id) FROM stdin;
1	John F. Kennedy International Airport	JFK	KJFK	40.641300	-73.778100	16
2	Los Angeles International Airport	LAX	KLAX	33.941600	-118.408500	16
3	London Heathrow Airport	LHR	EGLL	51.469400	-0.454400	17
4	Charles de Gaulle Airport	CDG	LFPG	49.009700	2.547900	18
5	Sydney Kingsford Smith Airport	SYD	YSSY	-33.946100	151.177200	19
6	Tokyo Narita International Airport	NRT	RJAA	35.765300	140.385400	13
7	Dubai International Airport	DBI	OMDB	25.253200	55.365700	12
8	Beijing Capital International Airport	PEK	ZBAA	40.079900	116.603100	5
9	Munich Airport	MUC	EDDM	48.353700	11.786100	1
10	Hong Kong International Airport	HKG	VHHH	22.308000	113.918500	5
11	Amsterdam Airport Schiphol	AMS	EHAM	52.310500	4.768300	7
12	Toronto Pearson International Airport	YYZ	CYYZ	43.677700	-79.624800	11
13	Auckland Airport	AKL	NZAA	-37.008100	174.791200	47
14	Incheon International Airport	ICN	RKSI	37.469200	126.450500	8
15	Istanbul Airport	IST	LTFM	41.275700	28.751900	23
16	Singapore Changi Airport	SIN	WSSS	1.364400	103.991500	12
17	Denver International Airport	DEN	KDEN	39.861700	-104.673100	16
18	Rome Fiumicino Airport	FCO	LIRF	41.800300	12.238300	10
19	San Francisco International Airport	SFO	KSFO	37.618800	-122.375000	16
20	Dublin Airport	DUB	EIDW	53.427600	-6.244200	20
\.


--
-- Data for Name: booking; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.booking (booking_id, booking_status, booking_made, flight_id, person_id, seat_id) FROM stdin;
9	confirmed	2023-12-28 14:33:34.681467	1	53	20
\.


--
-- Data for Name: booking_payment; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.booking_payment (booking_payment_id, booking_id, amount, payment_method, payment_status) FROM stdin;
11	9	27.59	payment card	successful
12	9	27.59	payment card	successful
13	9	27.59	payment card	successful
\.


--
-- Data for Name: city; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.city (city_id, city) FROM stdin;
1	Munich
2	Barcelona
3	São Paulo
4	Bangalore
5	Shanghai
6	Mexico City
7	Amsterdam
8	Seoul
9	Stockholm
10	Milan
11	Montreal
12	Dubai
13	Tokyo
14	Moscow
15	Buenos Aires
16	New York
17	London
18	Paris
19	Sydney
20	Stuttgart
21	Seville
22	Dublin
23	Athens
24	Lisbon
25	Istanbul
26	Oslo
27	Warsaw
28	Zurich
29	Vienna
30	Brussels
31	Prague
32	Helsinki
33	Copenhagen
34	Budapest
35	Bucharest
36	Sofia
37	Zagreb
38	Ljubljana
39	Belgrade
40	Sarajevo
41	Podgorica
42	Tirana
43	Skopje
44	Pristina
45	Luxembourg City
46	Tallinn
\.


--
-- Data for Name: country; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.country (country_id, country) FROM stdin;
1	Germany
2	Spain
3	Brazil
4	India
5	China
6	Mexico
7	Netherlands
8	South Korea
9	Sweden
10	Italy
11	Canada
12	United Arab Emirates
13	Japan
14	Russia
15	Argentina
16	United States
17	United Kingdom
18	France
19	Australia
20	Ireland
21	Greece
22	Portugal
23	Turkey
24	Norway
25	Poland
26	Switzerland
27	Austria
28	Belgium
29	Czech Republic
30	Finland
31	Denmark
32	Hungary
33	Romania
34	Bulgaria
35	Croatia
36	Slovenia
37	Serbia
38	Bosnia and Herzegovina
39	Montenegro
40	Albania
41	Macedonia
42	Kosovo
43	Luxembourg
44	Estonia
45	Slovakia
46	Venezuela
47	New Zealand
48	Malaysia
\.


--
-- Data for Name: flight_schedule; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.flight_schedule (flight_id, origin_airport_id, destination_airport_id, aircraft_id, departure_dt, arrival_dt, is_scheduled) FROM stdin;
1	2	8	15	2023-10-30 15:35:00	2023-10-30 23:58:00	t
2	4	10	28	2023-11-01 11:35:00	2023-11-01 13:35:00	t
3	9	1	11	2023-11-01 13:02:00	2023-11-01 13:47:00	t
4	14	5	7	2023-11-01 13:35:00	2023-11-01 13:35:00	t
5	8	2	13	2023-11-02 15:35:00	2023-11-02 20:35:00	t
6	2	6	24	2023-11-02 15:35:00	2023-11-02 19:35:00	t
7	5	18	14	2023-11-02 19:35:00	2023-11-02 19:35:00	t
8	4	19	47	2023-11-02 17:35:00	2023-11-02 22:38:00	t
9	13	8	39	2023-11-02 13:35:00	2023-11-02 18:02:00	t
10	1	18	14	2023-11-02 21:35:00	2023-11-02 23:59:00	f
\.


--
-- Data for Name: person; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.person (person_id, first_name, family_name, date_of_birth, gender, email, phone, address_id, balance, password_hash) FROM stdin;
57	\N	\N	\N	\N	tomas@email.com	\N	\N	\N	$argon2id$v=19$m=65536,t=10,p=1$XifL97oNZaN/4CqsI5X+dw$21fPmxJpSabEgc78bpsAs4VwTTQzk7JiYzMEPRsXmUw
53	Petr	Zelinka	2002-12-16	male	petr@email.com	+420702900081	6	10985	$argon2id$v=19$m=65536,t=10,p=1$oLflQGQhIigJhZt0fWjJPw$zVax/mU43aueDHPScFI8zcoPShYsA+0kBfY7XKaQOVc
\.


--
-- Data for Name: person_has_role; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.person_has_role (person_id, role_id) FROM stdin;
53	1
57	1
\.


--
-- Data for Name: role; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.role (role_id, role) FROM stdin;
2	administrator
1	client
\.


--
-- Data for Name: seat; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.seat (seat_id, seat_number, aircraft_model_id, travel_class_id) FROM stdin;
1	1	1	1
2	2	1	1
3	3	1	1
4	4	1	1
5	5	1	1
11	1	2	1
12	2	2	1
13	3	2	1
14	4	2	1
21	1	3	1
22	2	3	1
31	1	4	1
32	2	4	1
33	3	4	1
34	4	4	1
35	5	4	1
36	6	4	1
37	7	4	1
41	1	5	1
42	2	5	1
43	3	5	1
44	4	5	1
51	1	6	1
52	2	6	1
53	3	6	1
54	4	6	1
55	5	6	1
56	6	6	1
57	7	6	1
58	8	6	1
59	9	6	1
60	10	6	1
61	1	7	1
62	2	7	1
63	3	7	1
71	1	8	1
72	2	8	1
73	3	8	1
6	6	1	3
7	7	1	3
8	8	1	3
9	9	1	4
10	10	1	4
15	5	2	3
16	6	2	3
17	7	2	3
18	8	2	3
19	9	2	4
20	10	2	4
23	3	3	2
24	4	3	2
25	5	3	3
26	6	3	3
27	7	3	3
28	8	3	4
29	9	3	4
30	10	3	4
38	8	4	2
39	9	4	2
40	10	4	2
45	5	5	3
46	6	5	3
47	7	5	3
48	8	5	3
49	9	5	3
50	10	5	4
64	4	7	2
65	5	7	2
66	6	7	3
67	7	7	3
68	8	7	3
69	9	7	4
70	10	7	4
74	4	8	2
75	5	8	2
76	6	8	2
77	7	8	2
78	8	8	2
79	9	8	4
80	10	8	4
\.


--
-- Data for Name: sqlinjection; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.sqlinjection (person_id, first_name, last_name) FROM stdin;
1	Petr	Zelinka
2	Aleš	Novák
3	Franta	Levák
4	Viktor	Vysoký
\.


--
-- Data for Name: travel_class; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.travel_class (travel_class_id, class) FROM stdin;
1	economy
2	economy+
3	business
4	first class
\.


--
-- Name: address_address_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.address_address_id_seq', 47, true);


--
-- Name: aircraft_aircraft_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.aircraft_aircraft_id_seq', 50, true);


--
-- Name: aircraft_model_aircraft_model_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.aircraft_model_aircraft_model_id_seq', 8, true);


--
-- Name: airport_airport_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.airport_airport_id_seq', 20, true);


--
-- Name: booking_booking_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.booking_booking_id_seq', 10, true);


--
-- Name: booking_payment_booking_payment_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.booking_payment_booking_payment_id_seq', 13, true);


--
-- Name: city_city_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.city_city_id_seq', 46, true);


--
-- Name: country_country_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.country_country_id_seq', 48, true);


--
-- Name: flight_schedule_flight_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.flight_schedule_flight_id_seq', 10, true);


--
-- Name: person_person_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.person_person_id_seq', 61, true);


--
-- Name: role_role_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.role_role_id_seq', 3, true);


--
-- Name: seat_seat_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.seat_seat_id_seq', 80, true);


--
-- Name: sqlinjection_person_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.sqlinjection_person_id_seq', 4, true);


--
-- Name: travel_class_travel_class_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.travel_class_travel_class_id_seq', 4, true);


--
-- Name: address address_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.address
    ADD CONSTRAINT address_pkey PRIMARY KEY (address_id);


--
-- Name: aircraft aircraft_aircraft_id_key; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.aircraft
    ADD CONSTRAINT aircraft_aircraft_id_key UNIQUE (aircraft_id);


--
-- Name: aircraft_model aircraft_model_model_key; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.aircraft_model
    ADD CONSTRAINT aircraft_model_model_key UNIQUE (model);


--
-- Name: aircraft_model aircraft_model_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.aircraft_model
    ADD CONSTRAINT aircraft_model_pkey PRIMARY KEY (aircraft_model_id);


--
-- Name: aircraft_model_travel_class_pricing aircraft_model_travel_class_pricing_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.aircraft_model_travel_class_pricing
    ADD CONSTRAINT aircraft_model_travel_class_pricing_pkey PRIMARY KEY (aircraft_model_id, travel_class_id);


--
-- Name: aircraft aircraft_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.aircraft
    ADD CONSTRAINT aircraft_pkey PRIMARY KEY (aircraft_id, aircraft_model_id);


--
-- Name: aircraft aircraft_registration_code_key; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.aircraft
    ADD CONSTRAINT aircraft_registration_code_key UNIQUE (registration_code);


--
-- Name: airport airport_name_iata_code_icao_code_key; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.airport
    ADD CONSTRAINT airport_name_iata_code_icao_code_key UNIQUE (name, iata_code, icao_code);


--
-- Name: airport airport_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.airport
    ADD CONSTRAINT airport_pkey PRIMARY KEY (airport_id);


--
-- Name: booking booking_booking_id_key; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.booking
    ADD CONSTRAINT booking_booking_id_key UNIQUE (booking_id);


--
-- Name: booking_payment booking_payment_booking_payment_id_key; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.booking_payment
    ADD CONSTRAINT booking_payment_booking_payment_id_key UNIQUE (booking_payment_id);


--
-- Name: booking_payment booking_payment_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.booking_payment
    ADD CONSTRAINT booking_payment_pkey PRIMARY KEY (booking_payment_id, booking_id);


--
-- Name: booking booking_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.booking
    ADD CONSTRAINT booking_pkey PRIMARY KEY (booking_id, flight_id, person_id, seat_id);


--
-- Name: city city_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.city
    ADD CONSTRAINT city_pkey PRIMARY KEY (city_id);


--
-- Name: person constraint_name; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.person
    ADD CONSTRAINT constraint_name UNIQUE (email);


--
-- Name: country country_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.country
    ADD CONSTRAINT country_pkey PRIMARY KEY (country_id);


--
-- Name: flight_schedule flight_schedule_flight_id_key; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.flight_schedule
    ADD CONSTRAINT flight_schedule_flight_id_key UNIQUE (flight_id);


--
-- Name: flight_schedule flight_schedule_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.flight_schedule
    ADD CONSTRAINT flight_schedule_pkey PRIMARY KEY (flight_id, origin_airport_id, destination_airport_id, aircraft_id);


--
-- Name: person_has_role person_has_role_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.person_has_role
    ADD CONSTRAINT person_has_role_pkey PRIMARY KEY (person_id, role_id);


--
-- Name: person person_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.person
    ADD CONSTRAINT person_pkey PRIMARY KEY (person_id);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (role_id);


--
-- Name: seat seat_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.seat
    ADD CONSTRAINT seat_pkey PRIMARY KEY (seat_id, seat_number, aircraft_model_id, travel_class_id);


--
-- Name: seat seat_seat_id_key; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.seat
    ADD CONSTRAINT seat_seat_id_key UNIQUE (seat_id);


--
-- Name: travel_class travel_class_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.travel_class
    ADD CONSTRAINT travel_class_pkey PRIMARY KEY (travel_class_id);


--
-- Name: address address_city_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.address
    ADD CONSTRAINT address_city_id_fkey FOREIGN KEY (city_id) REFERENCES bds.city(city_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: address address_country_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.address
    ADD CONSTRAINT address_country_id_fkey FOREIGN KEY (country_id) REFERENCES bds.country(country_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: aircraft aircraft_aircraft_model_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.aircraft
    ADD CONSTRAINT aircraft_aircraft_model_id_fkey FOREIGN KEY (aircraft_model_id) REFERENCES bds.aircraft_model(aircraft_model_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: aircraft_model_travel_class_pricing aircraft_model_travel_class_pricing_aircraft_model_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.aircraft_model_travel_class_pricing
    ADD CONSTRAINT aircraft_model_travel_class_pricing_aircraft_model_id_fkey FOREIGN KEY (aircraft_model_id) REFERENCES bds.aircraft_model(aircraft_model_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: aircraft_model_travel_class_pricing aircraft_model_travel_class_pricing_travel_class_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.aircraft_model_travel_class_pricing
    ADD CONSTRAINT aircraft_model_travel_class_pricing_travel_class_id_fkey FOREIGN KEY (travel_class_id) REFERENCES bds.travel_class(travel_class_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: airport airport_country_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.airport
    ADD CONSTRAINT airport_country_id_fkey FOREIGN KEY (country_id) REFERENCES bds.country(country_id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: booking booking_flight_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.booking
    ADD CONSTRAINT booking_flight_id_fkey FOREIGN KEY (flight_id) REFERENCES bds.flight_schedule(flight_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: booking_payment booking_payment_booking_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.booking_payment
    ADD CONSTRAINT booking_payment_booking_id_fkey FOREIGN KEY (booking_id) REFERENCES bds.booking(booking_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: booking booking_person_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.booking
    ADD CONSTRAINT booking_person_id_fkey FOREIGN KEY (person_id) REFERENCES bds.person(person_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: booking booking_seat_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.booking
    ADD CONSTRAINT booking_seat_id_fkey FOREIGN KEY (seat_id) REFERENCES bds.seat(seat_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: flight_schedule flight_schedule_aircraft_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.flight_schedule
    ADD CONSTRAINT flight_schedule_aircraft_id_fkey FOREIGN KEY (aircraft_id) REFERENCES bds.aircraft(aircraft_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: flight_schedule flight_schedule_destination_airport_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.flight_schedule
    ADD CONSTRAINT flight_schedule_destination_airport_id_fkey FOREIGN KEY (destination_airport_id) REFERENCES bds.airport(airport_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: flight_schedule flight_schedule_origin_airport_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.flight_schedule
    ADD CONSTRAINT flight_schedule_origin_airport_id_fkey FOREIGN KEY (origin_airport_id) REFERENCES bds.airport(airport_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: person person_address_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.person
    ADD CONSTRAINT person_address_id_fkey FOREIGN KEY (address_id) REFERENCES bds.address(address_id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: person_has_role person_has_role_person_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.person_has_role
    ADD CONSTRAINT person_has_role_person_id_fkey FOREIGN KEY (person_id) REFERENCES bds.person(person_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: person_has_role person_has_role_role_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.person_has_role
    ADD CONSTRAINT person_has_role_role_id_fkey FOREIGN KEY (role_id) REFERENCES bds.role(role_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: seat seat_aircraft_model_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.seat
    ADD CONSTRAINT seat_aircraft_model_id_fkey FOREIGN KEY (aircraft_model_id) REFERENCES bds.aircraft_model(aircraft_model_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: seat seat_travel_class_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.seat
    ADD CONSTRAINT seat_travel_class_id_fkey FOREIGN KEY (travel_class_id) REFERENCES bds.travel_class(travel_class_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: SCHEMA bds; Type: ACL; Schema: -; Owner: postgres
--

GRANT USAGE ON SCHEMA bds TO bds_app;


--
-- Name: FUNCTION armor(bytea); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.armor(bytea) TO bds_app;


--
-- Name: FUNCTION armor(bytea, text[], text[]); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.armor(bytea, text[], text[]) TO bds_app;


--
-- Name: FUNCTION crypt(text, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.crypt(text, text) TO bds_app;


--
-- Name: FUNCTION dearmor(text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.dearmor(text) TO bds_app;


--
-- Name: FUNCTION decrypt(bytea, bytea, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.decrypt(bytea, bytea, text) TO bds_app;


--
-- Name: FUNCTION decrypt_iv(bytea, bytea, bytea, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.decrypt_iv(bytea, bytea, bytea, text) TO bds_app;


--
-- Name: FUNCTION digest(bytea, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.digest(bytea, text) TO bds_app;


--
-- Name: FUNCTION digest(text, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.digest(text, text) TO bds_app;


--
-- Name: FUNCTION encrypt(bytea, bytea, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.encrypt(bytea, bytea, text) TO bds_app;


--
-- Name: FUNCTION encrypt_iv(bytea, bytea, bytea, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.encrypt_iv(bytea, bytea, bytea, text) TO bds_app;


--
-- Name: FUNCTION gen_random_bytes(integer); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.gen_random_bytes(integer) TO bds_app;


--
-- Name: FUNCTION gen_random_uuid(); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.gen_random_uuid() TO bds_app;


--
-- Name: FUNCTION gen_salt(text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.gen_salt(text) TO bds_app;


--
-- Name: FUNCTION gen_salt(text, integer); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.gen_salt(text, integer) TO bds_app;


--
-- Name: FUNCTION hmac(bytea, bytea, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.hmac(bytea, bytea, text) TO bds_app;


--
-- Name: FUNCTION hmac(text, text, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.hmac(text, text, text) TO bds_app;


--
-- Name: FUNCTION pgp_armor_headers(text, OUT key text, OUT value text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.pgp_armor_headers(text, OUT key text, OUT value text) TO bds_app;


--
-- Name: FUNCTION pgp_key_id(bytea); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.pgp_key_id(bytea) TO bds_app;


--
-- Name: FUNCTION pgp_pub_decrypt(bytea, bytea); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.pgp_pub_decrypt(bytea, bytea) TO bds_app;


--
-- Name: FUNCTION pgp_pub_decrypt(bytea, bytea, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.pgp_pub_decrypt(bytea, bytea, text) TO bds_app;


--
-- Name: FUNCTION pgp_pub_decrypt(bytea, bytea, text, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.pgp_pub_decrypt(bytea, bytea, text, text) TO bds_app;


--
-- Name: FUNCTION pgp_pub_decrypt_bytea(bytea, bytea); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.pgp_pub_decrypt_bytea(bytea, bytea) TO bds_app;


--
-- Name: FUNCTION pgp_pub_decrypt_bytea(bytea, bytea, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.pgp_pub_decrypt_bytea(bytea, bytea, text) TO bds_app;


--
-- Name: FUNCTION pgp_pub_decrypt_bytea(bytea, bytea, text, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.pgp_pub_decrypt_bytea(bytea, bytea, text, text) TO bds_app;


--
-- Name: FUNCTION pgp_pub_encrypt(text, bytea); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.pgp_pub_encrypt(text, bytea) TO bds_app;


--
-- Name: FUNCTION pgp_pub_encrypt(text, bytea, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.pgp_pub_encrypt(text, bytea, text) TO bds_app;


--
-- Name: FUNCTION pgp_pub_encrypt_bytea(bytea, bytea); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.pgp_pub_encrypt_bytea(bytea, bytea) TO bds_app;


--
-- Name: FUNCTION pgp_pub_encrypt_bytea(bytea, bytea, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.pgp_pub_encrypt_bytea(bytea, bytea, text) TO bds_app;


--
-- Name: FUNCTION pgp_sym_decrypt(bytea, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.pgp_sym_decrypt(bytea, text) TO bds_app;


--
-- Name: FUNCTION pgp_sym_decrypt(bytea, text, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.pgp_sym_decrypt(bytea, text, text) TO bds_app;


--
-- Name: FUNCTION pgp_sym_decrypt_bytea(bytea, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.pgp_sym_decrypt_bytea(bytea, text) TO bds_app;


--
-- Name: FUNCTION pgp_sym_decrypt_bytea(bytea, text, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.pgp_sym_decrypt_bytea(bytea, text, text) TO bds_app;


--
-- Name: FUNCTION pgp_sym_encrypt(text, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.pgp_sym_encrypt(text, text) TO bds_app;


--
-- Name: FUNCTION pgp_sym_encrypt(text, text, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.pgp_sym_encrypt(text, text, text) TO bds_app;


--
-- Name: FUNCTION pgp_sym_encrypt_bytea(bytea, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.pgp_sym_encrypt_bytea(bytea, text) TO bds_app;


--
-- Name: FUNCTION pgp_sym_encrypt_bytea(bytea, text, text); Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON FUNCTION bds.pgp_sym_encrypt_bytea(bytea, text, text) TO bds_app;


--
-- Name: TABLE address; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE bds.address TO bds_app;


--
-- Name: SEQUENCE address_address_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,USAGE ON SEQUENCE bds.address_address_id_seq TO bds_app;


--
-- Name: TABLE aircraft; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE bds.aircraft TO bds_app;


--
-- Name: SEQUENCE aircraft_aircraft_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,USAGE ON SEQUENCE bds.aircraft_aircraft_id_seq TO bds_app;


--
-- Name: TABLE aircraft_model; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE bds.aircraft_model TO bds_app;


--
-- Name: SEQUENCE aircraft_model_aircraft_model_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,USAGE ON SEQUENCE bds.aircraft_model_aircraft_model_id_seq TO bds_app;


--
-- Name: TABLE aircraft_model_travel_class_pricing; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE bds.aircraft_model_travel_class_pricing TO bds_app;


--
-- Name: TABLE airport; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE bds.airport TO bds_app;


--
-- Name: SEQUENCE airport_airport_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,USAGE ON SEQUENCE bds.airport_airport_id_seq TO bds_app;


--
-- Name: TABLE booking; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE bds.booking TO bds_app;


--
-- Name: SEQUENCE booking_booking_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,USAGE ON SEQUENCE bds.booking_booking_id_seq TO bds_app;


--
-- Name: TABLE booking_payment; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE bds.booking_payment TO bds_app;


--
-- Name: SEQUENCE booking_payment_booking_payment_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,USAGE ON SEQUENCE bds.booking_payment_booking_payment_id_seq TO bds_app;


--
-- Name: TABLE city; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE bds.city TO bds_app;


--
-- Name: SEQUENCE city_city_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,USAGE ON SEQUENCE bds.city_city_id_seq TO bds_app;


--
-- Name: TABLE country; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE bds.country TO bds_app;


--
-- Name: SEQUENCE country_country_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,USAGE ON SEQUENCE bds.country_country_id_seq TO bds_app;


--
-- Name: TABLE flight_schedule; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE bds.flight_schedule TO bds_app;


--
-- Name: SEQUENCE flight_schedule_flight_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,USAGE ON SEQUENCE bds.flight_schedule_flight_id_seq TO bds_app;


--
-- Name: TABLE person; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE bds.person TO bds_app;


--
-- Name: TABLE person_has_role; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE bds.person_has_role TO bds_app;


--
-- Name: SEQUENCE person_person_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,USAGE ON SEQUENCE bds.person_person_id_seq TO bds_app;


--
-- Name: TABLE role; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE bds.role TO bds_app;


--
-- Name: SEQUENCE role_role_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,USAGE ON SEQUENCE bds.role_role_id_seq TO bds_app;


--
-- Name: TABLE seat; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE bds.seat TO bds_app;


--
-- Name: SEQUENCE seat_seat_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,USAGE ON SEQUENCE bds.seat_seat_id_seq TO bds_app;


--
-- Name: TABLE sqlinjection; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE bds.sqlinjection TO bds_app;


--
-- Name: SEQUENCE sqlinjection_person_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,USAGE ON SEQUENCE bds.sqlinjection_person_id_seq TO bds_app;


--
-- Name: TABLE travel_class; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE bds.travel_class TO bds_app;


--
-- Name: SEQUENCE travel_class_travel_class_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT SELECT,USAGE ON SEQUENCE bds.travel_class_travel_class_id_seq TO bds_app;


--
-- PostgreSQL database dump complete
--

