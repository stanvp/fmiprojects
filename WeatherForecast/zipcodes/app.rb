require 'sinatra'
require 'sequel'

Sequel::Model.plugin :xml_serializer

DB = Sequel.sqlite('zipcodes.db')

class City < Sequel::Model; end

def cities(city, limit)
  City.filter(/\d{5}(-\d{4})?/.match(city) ? :zip.like(city) : :city.like(city)).limit(limit)
end

get '/zipcodes/:city/:limit' do |city, limit|
  content_type 'text/xml', :charset => 'utf-8'
  cities(city, limit).to_xml 
end

get '/zipcodes/:city/:limit/:group' do |city, limit, group|
  content_type 'text/xml', :charset => 'utf-8'
  cities(city, limit).group(group.to_sym).to_xml   
end